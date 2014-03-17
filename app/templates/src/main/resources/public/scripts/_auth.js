/**
 * Authentication initializer and custom authenticator
 * Docs:
 * http://ember-simple-auth.simplabs.com/
 */

Ember.Application.initializer({
    name: 'authentication',
    initialize: function (container, application) {
        var options = {
            routeAfterInvalidation: "/login"
        };

        Ember.SimpleAuth.Session.reopen({
            currentUser: function() {
                var username = this.get('username');

                if (!Ember.isEmpty(username)) {
                    return container.lookup('store:main').find('user', username);
                }
            }.property('username')
        });

        container.register('app:authenticators:custom', JhipsterEmber.CustomAuthenticator);

        Ember.SimpleAuth.setup(container, application, options);
    }
});


<%= _.classify(appname) %>.CustomAuthenticator = Ember.SimpleAuth.Authenticators.OAuth2.extend({
    authenticate: function (credentials) {
        return new Ember.RSVP.Promise(function (resolve, reject) {
            credentials.identification = credentials.identification.trim();
            credentials.password = credentials.password.trim();

            Ember.$.ajax({
                url: ENV.api_token_endpoint,
                type: 'POST',
                headers: {"Authorization": "Basic d2ViOg=="},
                data: {
                    client_id: 'web',
                    grant_type: 'password',
                    username: credentials.identification,
                    password: credentials.password
                }
            }).then(function (response) {
                Ember.run(function () {
                    resolve({
                        access_token: response.access_token,
                        username: response.username
                    });
                });
            }, function (xhr, status, error) {
                Ember.run(function () {
                    reject(xhr.responseText);
                });
            });
        });
    }
});
