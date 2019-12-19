# MultiPurposeDownloader

This library was created to handle the tedious issues of file management and images display.

## Installation

For the installation you must add in your gradle the following

```bash
implement "com.github.Crdzbird.MultiPurposeDownloader:RELEASE_VERSION"
```

## Usage


### file
```kotlin
MultiDataParser().obtainFile(context)
                .setFileName("{System.currentTimeMillis()}")
                .setNotificationEnabled(true, getString(R.string.app_name))
                .setOnDownloadResultListener(object : OnDownloadResult {
                    override fun onSuccess(filePath: String) {
                        runOnUiThread{
                            Toast.makeText(context, String.format("File downloaded at: %s", filePath), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(error: String, errorCode:Int) {
                        runOnUiThread{
                            Toast.makeText(context, String.format("File downloading error. ERROR_CODE_%s", errorCode), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .download(url!!, FileTypes.TYPE_IMAGE_JPG)
```

### image
```kotlin
imageWidget?.let { MultiDataParser().obtainImage(context).load(it, url) }
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
