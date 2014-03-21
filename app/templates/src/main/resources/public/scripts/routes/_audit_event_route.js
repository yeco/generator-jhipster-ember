<%= _.classify(appname) %>.AuditEventRoute = Ember.Route.extend(Ember.SimpleAuth.AuthenticatedRouteMixin, {
    setupController: function (controller) {
        var self = this;
        this.get('store').find('auditEvent', {page: 0}).then(function (model) {
            controller.set('model', model);
            controller.set('meta', Ember.copy(self.get('store').metadataFor('auditEvent')));
        });
    }
});
