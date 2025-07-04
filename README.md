# example-percy-appium-java
Example app used by the [Percy Java Appium tutorial](https://docs.percy.io/v2-app/docs/appium-for-java) demonstrating Percy's Java Appium integration.

## Java Appium Tutorial

The tutorial assumes you're already familiar with Java and
[Appium](https://appium.io/) and focuses on using it with Percy. You'll still
be able to follow along if you're not familiar with Java Appium, but we won't
spend time introducing Java Appium concepts.


The tutorial also assumes you have [Node 12+ with
npm](https://nodejs.org/en/download/) and
[git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) installed.

### Step 1

Clone the example application and install dependencies:

```bash
$ git clone https://github.com/percy/example-percy-appium-java.git
$ cd example-percy-appium-java
```

This tutorial specifically uses Browserstack App Automate to run appium test, even though you should be able to follow the tutorial if you are using any other service to run appium tests or running it locally. For app percy to work we only need to initialize AppPercy with driver object.

In case you are choosing to use Browserstack App Automate, please follow following steps:

1. You will need a BrowserStack `username` and `access key`. To obtain your access credentials, [sign up](https://www.browserstack.com/users/sign_up?utm_campaign=Search-Brand-India&utm_source=google&utm_medium=cpc&utm_content=609922405128&utm_term=browserstack) for a free trial or [purchase a plan](https://www.browserstack.com/pricing).

2. Please get your `username` and `access key` from [profile](https://www.browserstack.com/accounts/profile) page.

Example Android and iOS apps are provided in [`resources/`](https://github.com/percy/example-percy-appium-js/blob/master/resources) folder.
You can upload them using following curl command
```bash
$ cd src/main/java/io/percy/resources
$ curl -u "<username>:<access key>" \
  -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
  -F "file=@IosApp.ipa" # for iOS, for android use AndroidApp.apk  
# You will get output similar to following, keep track of this app url
{"app_url":"bs://b3d22f77ac5c4064bc1892f1c470d3728a9b697f"}
```

You can also follow [`upload your app`](https://www.browserstack.com/docs/app-automate/appium/getting-started/java#2-upload-your-app) if using App Automate for latest instructions.

### Step 2

Sign in to Percy and create a new `app` type project. You can name the project "test-project" if you'd like. After you've created the project, you'll be shown a token environment variable.

### Step 3

In the shell window you're working in, export the token environment variable:

**Unix**

``` shell
$ export PERCY_TOKEN="<your token here>"
```

**Windows**

``` shell
$ set PERCY_TOKEN="<your token here>"

# PowerShell
$ $Env:PERCY_TOKEN="<your token here>"
```

Note: Usually this would only be set up in your CI environment, but to keep things simple we'll
configure it in your shell so that Percy is enabled in your local environment.

### Step 4

Check out a new branch for your work in this tutorial (we'll call this branch
`tutorial-example`), then run tests & take screenshots:

``` shell
$ git checkout -b tutorial-example
$ npm install
$ npm install -g @percy/cli@latest
```
Minimum required `@percy/cli` version is 1.15.0 for this to work correctly. If you already have `@percy/cli` installed please
update it to latest or minium required version with `npm update -g @percy/cli`.

Now run the build with following commands

``` shell
# For Android
$ make test-android
# For iOS
$ make test-ios
```

This will run the app's Java Appium tests, which contain calls to create Percy screenshots. The screenshots
will then be uploaded to Percy for comparison. Percy will use the Percy token you used in **Step 2**
to know which organization and project to upload the screenshots to.

You can view the screenshots in Percy now if you want, but there will be no visual comparisons
yet. You'll see that Percy shows you that these screenshots come from your `tutorial-example` branch.

### Step 5

Depending on if you ran Android or iOS tests, please find one of the following lines in code and update it to change content on screen

For Android.java
```
textInput.sendKeys("Browserstack"); // Say change this to "App Percy"
```

For Ios.java
```
textInput.sendKeys("hello@percy.io\n"); // Say change this to "email@browserstack.com"
```

### Step 6

Commit the change:

``` shell
$ git commit -am "Changed text"
```

### Step 7

Run the tests with screenshots again:

``` shell
# For Android
$ make test-android
# For iOS
$ make test-ios
```

This will run the tests again and take new screenshots of our modified application. The new screenshots
will be uploaded to Percy and compared with the previous screenshots, showing any visual diffs.

At the end of the test run output, you will see logs from Percy confirming that the screenshots were
successfully uploaded and giving you a direct URL to check out any visual diffs.

### Step 8

Visit your project in Percy and you'll see a new build with the visual comparisons between the two
runs. Click anywhere on the Build 2 row. You can see the original screenshots on the left, and the new
screenshots on the right.

Percy has highlighted what's changed visually in the app! Snapshots with the largest changes are
shown first You can click on the highlight to reveal the underlying screenshot.

If you scroll down, you'll see that no other test cases were impacted by our changes to text. 
The unchanged screenshots are shown under `Unchanged` filter and are hidden by default.

### Finished! 😀

From here, you can try making your own changes to the app and tests, if you like. If you do, re-run
the tests and you'll see any visual changes reflected in Percy.
