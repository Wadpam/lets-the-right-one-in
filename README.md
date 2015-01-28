# Lets the Right One In

Lets the Right One In (paraphrase of the excellent
  [novel](http://en.wikipedia.org/wiki/Let_the_Right_One_In_%28novel%29) and
  [movie](http://www.imdb.com/title/tt1139797/)) or lroi for short is an Android
  library that provides authentication for services based on [Guja](https://github.com/Wadpam/guja).

Features:

- Compatible with [Guja](https://github.com/Wadpam/guja)
- Provides authentication based on [AccountManager](http://developer.android.com/reference/android/accounts/AccountManager.html) accounts.
- Aimed for projects that uses Retrofit + OkHttp (unsuitable for projects that don't). __This might change in future releases.__


## Usage

Follow the steps below to use this library. There's also an example app in the `/sample` folder.

### Step 1

Create a class that extends `se.leiflandia.lroi.auth.AuthenticationService`.

### Step 2

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


### Step 3

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


### Step 4

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
### Step 5

Use the instance of Authadapter to perform sign up, sign in and sign out operations. It can also create
instances of com.squareup.okhttp.Authenticator and retrofit.RequestInterceptor that can be used
together with OkHttp and Retrofit to automatically include the access token in http requests and refresh when access token have expired.
