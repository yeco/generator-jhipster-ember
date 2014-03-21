<%= _.classify(appname) %>.LoginController = Ember.ObjectController.extend(Ember.SimpleAuth.LoginControllerMixin, {
    identification: "",
    password: "",
    authenticatorFactory: "authenticators:custom"
});
