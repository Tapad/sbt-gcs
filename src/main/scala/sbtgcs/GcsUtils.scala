package sbtgcs
import java.io.{File, FileInputStream}

import com.google.cloud.storage
import com.google.cloud.storage._
import sbt.Logger

object GcsUtils {
  def upload(log: Logger, projectId: String, source: File, destination: String, overwrite: Boolean, mimeType: String): Unit = {
    val regex = "gs://([^/]+)/(.+)".r
    destination match {
      case regex(bucketName, blobName) =>
        val bucket = Storage(projectId).getOrCreateBucket(bucketName)
        bucket.getBlobOpt(blobName) match {
          case Some(b) if !overwrite => sys.error(s"Cannot overwrite existing Blob: ${b.getLocation}")
          case _ =>
            log.info(s"Uploading ${source.getAbsolutePath} to gs://$bucketName/$blobName ...")
            val b = bucket.createBlob(blobName, source, mimeType)
            log.info(s"Uploaded into ${b.getLocation}")
        }
      case _ => sys.error(s"Destination has to match $regex, but is $destination instead")
    }
  }

  def blobName(organization: String, name: String, version: String): String = {
    val orgSubPath = organization.replace(".", "/")
    val repo = if (version.endsWith("-SNAPSHOT")) "snapshots" else "releases"
    s"$repo/$orgSubPath/$name/$version/$name-$version.jar"
  }

  private class Storage(private val projectId: String, private val storage: com.google.cloud.storage.Storage) {
    def getBucketOpt(name: String): scala.Option[Bucket] = scala.Option(storage.get(name)).map(Bucket)
    def createBucket(name: String): Bucket = Bucket(storage.create(BucketInfo.of(name)))
    def getOrCreateBucket(name: String): Bucket = getBucketOpt(name).getOrElse(createBucket(name))
  }

  private object Storage {
    def apply(projectId: String): Storage = {
      val service: storage.Storage = StorageOptions
        .newBuilder()
        .setProjectId(projectId)
        .build()
        .getService
      new Storage(projectId, service)
    }
  }

  private case class Bucket(private val bucket: com.google.cloud.storage.Bucket) {
    def getBlobOpt(blobName: String): scala.Option[Blob] = scala.Option(bucket.get(blobName)).map(Blob)
    def createBlob(name: String, file: File, contentType: String): Blob =
      Blob(bucket.create(name, new FileInputStream(file), contentType))
  }

  private case class Blob(private val blob: com.google.cloud.storage.Blob) {
    def getLocation: String = s"gs://${blob.getBucket}/${blob.getName}"
    def exists: Boolean = blob.exists()
  }
}
