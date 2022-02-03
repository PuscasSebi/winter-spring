## Authorization mysql

This is an authorization server based on spring oauth2.

We have some user and role tables that we are going to rely when authenticating here

and we have some oauth2 tables from spring which we are going to rely upon on whole authorization mechanism


need to create database from schema.sql..
then to insert some user from data.sql..


<script>
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '{your-app-id}',
      cookie     : true,
      xfbml      : true,
      version    : '{api-version}'
    });

    FB.AppEvents.logPageView();

  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "https://connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));
</script>


<fb:login-button
  scope="public_profile,email"
  onlogin="checkLoginState();">
</fb:login-button>


function checkLoginState() {
  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });
}