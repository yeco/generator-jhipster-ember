<%= _.classify(appname) %>.ApplicationController = Ember.ObjectController.extend({
    hasRootRole: function () {
        var roles = this.get('session.roles');
        if (!Ember.isEmpty(roles)) {
            if (roles instanceof Array) {
                return roles.contains('ROOT');
            } else {
                return roles.split(',').contains('ROOT');
            }
        }
    }.property('session.roles')
});
