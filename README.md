# Lets the Right One In

Lets the Right One In (paraphrase of the excellent
  [novel](http://en.wikipedia.org/wiki/Let_the_Right_One_In_%28novel%29) and
  [movie](http://www.imdb.com/title/tt1139797/)) or lroi for short is an Android
  library that provides authentication for services based on [Guja](https://github.com/Wadpam/guja).

Features:

- Compatible with [Guja](https://github.com/Wadpam/guja)
- Provides authentication based on [AccountManager](http://developer.android.com/reference/android/accounts/AccountManager.html) accounts.
- Currently supports the [OAuth 2.0 Client Credentials Grant](http://tools.ietf.org/html/rfc6749#section-4.4) flow
- Aimed for projects that uses Retrofit + OkHttp (unsuitable for projects that don't). __This might change in future releases.__


## Usage

Follow the steps below to use this library. There's also a very basic example app in the `/sample` folder (please note that this sample doesn't make use of the com.squareup.okhttp.Authenticator and retrofit.RequestInterceptor classes that AuthAdapter can provide).

### Step 1

Add the following to your build file.

If you're using gradle:

```
repositories {
  maven {
    url "https://jitpack.io"
  }
}

dependencies {
  compile 'com.github.Wadpam:lets-the-right-one-in:v0.1.3'
}
```

If maven is your thing:

```
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.Wadpam</groupId>
  <artifactId>lets-the-right-one-in</artifactId>
  <version>v0.1.1</version>
</dependency>
```

### Step 2

Create a class that extends `se.leiflandia.lroi.auth.AuthenticationService`.

### Step 3

Add the service created in the last step to the manifest under `<application>`:

```
<service
    android:name=".ExampleAuthenticationService"
    android:exported="false"
    android:process=":auth" >
    <intent-filter>
        <action android:name="android.accounts.AccountAuthenticator" />
    </intent-filter>
    <meta-data
        android:name="android.accounts.AccountAuthenticator"
        android:resource="@xml/authenticator" />
</service>
```

Replace `.ExampleAuthenticationService` with your own implementation of `se.leiflandia.lroi.auth.AuthenticationService`.


### Step 4

Create the file `res/xml/authenticator.xml` with the following content:

```
<?xml version="1.0" encoding="utf-8"?>
<!-- Please note accountType needs to be the same as provided to AuthAdapter -->
<account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
    android:accountType="[Insert your account type here]"
    android:icon="@drawable/ic_launcher"
    android:smallIcon="@drawable/ic_launcher"
    android:label="[Insert your account label here]"
    />
```

Replace the values in brackets.


### Step 5

Create an instance of AuthAdapter, for example like this:

```
AuthAdapter auth = new AuthAdapter.Builder()
    .setAuthTokenType("org.example.account")
    .setAccountType("org.example.account")
    .setEndpoint("https://example.org")
    .setApplicationContext(this)
    .setClientCredentials(new ClientCredentials("clientId", "clientSecret"))
    .setLoginActivityClass(LoginActivity.class)
    .build();
```
### Step 6

Use the instance of Authadapter to perform sign up, sign in and sign out operations. It can also create
instances of com.squareup.okhttp.Authenticator and retrofit.RequestInterceptor that can be used
together with OkHttp and Retrofit to automatically include the access token in http requests and refresh when access token have expired.
