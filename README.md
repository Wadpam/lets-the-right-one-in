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


# Step 1

Create a class that extends se.leiflandia.lroi.auth.AuthenticationService.

# Step 2

Edit your apps manifest file and make sure it includes the following permissions:

```
<uses-permission android:name="android.permission.READ_PROFILE" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
<uses-permission android:name="android.permission.MANAGE_ACCOUNTS" />
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
```

Also add the service created in step 1 in the manifest under `<application>`:

```
<service
    android:name=".auth.FeederAuthenticationService"
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

# Step 3

Create the file `res/xml/authenticator.xml` with the following content:

```
<?xml version="1.0" encoding="utf-8"?>
<!-- Please note accountType needs to be the same as in se.pfeiff.feeder.auth.AuthConst -->
<account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
    android:accountType="[Insert your account type here]"
    android:icon="@drawable/ic_launcher"
    android:smallIcon="@drawable/ic_launcher"
    android:label="[Insert your account label here]"
    />
```

Replace the values in brackets.


# Step 4

Create an instance of AuthAdapter, for example like this:

```
AuthAdapter auth = new AuthAdapter.Builder()
    .setAuthTokenType("org.example.account")
    .setAccountType("org.example.account")
    .setEndpoint("https://example.org")
    .setAccountManager(AccountManager.get(this))
    .setApplicationContext(this)
    .setClientCredentials(new ClientCredentials("clientId", "clientSecret"))
    .build();
```

This instance can be used to perform sign up, sign in and sign out operations. It can also create
instances of com.squareup.okhttp.Authenticator and retrofit.RequestInterceptor that can be used
together with OkHttp and Retrofit.