<%= _.classify(appname) %>.ApplicationRoute = Ember.Route.extend(Ember.SimpleAuth.ApplicationRouteMixin, {
    actions: {
        sessionAuthenticationSucceeded: function () {
            var self = this;

            this.get('session.currentUser').then(function () {
                self.transitionTo(Ember.SimpleAuth.routeAfterAuthentication);
            });
        },
        sessionAuthenticationFailed: function (response) {
            var errorMessage = JSON.parse(response).message;
            Bootstrap.NM.push(errorMessage, 'danger');
            this.transitionTo(Ember.SimpleAuth.routeAfterInvalidation);
        }
    }
});
