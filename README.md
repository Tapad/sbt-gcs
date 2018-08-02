# sbt-gcs
An [sbt](http://scala-sbt.org) plugin for publishing artifacts to [Google Cloud Storage](https://cloud.google.com/storage/).
It is highly inspired by [Tapad/sbt-hadoop](https://github.com/tapad/sbt-hadoop-oss) plugin.

## Requirements
- sbt (0.13.5+ or 1.0.0+)

## Installation
Add the following line to `project/plugins.sbt`. See the [Using plugins](http://www.scala-sbt.org/release/docs/Using-Plugins.html) section of the sbt documentation for more information.

```
addSbtPlugin("com.tapad.sbt" % "sbt-gcs" % "0.1.1")
```

### Credentials
Authentication to Google Cloud Storage has to be provided. The following are searched (in order) to find the Application Default Credentials:

* Credentials file pointed to by the `GOOGLE_APPLICATION_CREDENTIALS` environment variable
* Credentials provided by the Google Cloud SDK gcloud auth application-default login command
* Google App Engine built-in credentials
* Google Cloud Shell built-in credentials
* Google Compute Engine built-in credentials

## Usage
Assuming that credentials are provided, one can just enable the plugin:

```
enablePlugins(GcsPlugin)
```

and set required settings:

```
gcsProjectId := "my-google-cloud-project"
gcsBucket := "gcs-bucket.tapad.com"
```

The location on GCS can be specfied using `gcsArtifactPath` setting. The default value is:

`gs://{gcsBucket}/{releases|snapshots}/{organization}/{name}/{version}/{name}-{version}.jar`

(e.g. `gs://gcs-bucket.tapad.com/snapshots/com/tapad/sbt/sbt-gcs/0.1.1-SNAPSHOT/sbt-gcs-0.1.1-SNAPSHOT.jar`)

### Integration with the built-in `packageBin` task
By default, sbt-gcs is configured to upload the resulting artifact of the `packageBin` task to GCS.

Once your build is properly configured, an invocation of `gcs:publish` will build, and subsequentially publish, your artifact to GCS.

For more information, refer to the [Packaging documentation](http://www.scala-sbt.org/0.13/docs/Howto-Package.html) provided in the sbt reference manual.

### Integration with sbt-assembly
To use sbt-gcs in conjunction with sbt-assembly, add the following to your `project/plugins.sbt` and `build.sbt` files, respectively:

```
addSbtPlugin("com.eed3sign" % "sbt-assembly" % "X.Y.Z")
addSbtPlugin("com.tapad.sbt" % "sbt-gcs" % "0.1.0")
```

```
gcsLocalArtifactPath := (assemblyOutputPath in assembly).value
gcsProjectId := "my-google-cloud-project"
gcsBucket := "gcs-bucket.tapad.com"
publish in Gcs := (publish in Gcs).dependsOn(assembly).value
```

Lastly, be sure to enable sbt-gcs in your `build.sbt` file:

```
enablePlugins(GcsPlugin)
```

sbt-assembly will be enabled automatically.

Once the build definition is configured properly, an invocation of `gcs:publish` will build and subsequentially publish a fat jar to GCS.

For more information, refer to the documentation provided by [sbt-assembly](https://github.com/sbt/sbt-assembly) and the scripted integration test found at [plugin/src/sbt-test/sbt-hadoop/assembly](plugin/src/sbt-test/sbt-hadoop/assembly).

## Contributing

#### root
The sbt plugin and underlying interface used to publish artifacts to GCS.

### Running tests
The main features and functionality of `sbt-gcs` are tested using sbt's [`scripted-plugin`](https://github.com/sbt/sbt/tree/0.13/scripted). `scripted` tests exist in the `src/sbt-test` directory of the root project.

To run these tests, issue `scripted` from an sbt session:

```
$ sbt
> scripted
```

To selectively run a single `scripted` test suite, issue `scripted <name of plugin>/<name of test project>`. e.g. `scripted sbt-gcs/simple`.

Please note that `publishLocal` will be invoked when running `scripted`. `scripted` tests take longer to run than unit tests and will log myriad output to stdout. Also note that any output written to stderr during the execution of a `scripted` test will result in `ERROR` level log entries. These log entries will not effect the resulting status of the actual test.

### Releasing artifacts
`sbt-gcs` uses [https://github.com/sbt/sbt-release](sbt-release). Simply invoke `release` from the root project to release all artifacts.
