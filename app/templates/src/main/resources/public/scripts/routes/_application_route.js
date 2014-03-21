<%= _.classify(appname) %>.ApplicationRoute = Ember.Route.extend(Ember.SimpleAuth.ApplicationRouteMixin, {
    actions: {
        sessionAuthenticationSucceeded: function () {
            var self = this;

            this.get('session.currentUser').then(function () {
                self.transitionTo(Ember.SimpleAuth.routeAfterAuthentication);
            });
        },
        sessionAuthenticationFailed: function () {
            Bootstrap.NM.push('Invalid username or password.', 'danger');
            this.transitionTo(Ember.SimpleAuth.routeAfterInvalidation);
        }
    }
});
