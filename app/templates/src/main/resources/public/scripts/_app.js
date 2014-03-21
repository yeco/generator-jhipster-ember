var <%= _.classify(appname) %> = window.<%= _.classify(appname) %> = Ember.Application.create({
    LOG_ACTIVE_GENERATION: true,
    LOG_TRANSITIONS: true,
    ENV_APP: false,
    LOG_TRANSITIONS_INTERNAL: true
});

Ember.ENV = window.ENV = {
    version: '0.0.0',
    api_url: '',
    api_namespace: 'api/v1',
    api_token_endpoint: '/oauth/token'
};

/* Order and include as you please. */
require('scripts/auth');
require('scripts/controllers/*');
require('scripts/store');
require('scripts/helpers');
require('scripts/models/*');
require('scripts/routes/*');
require('scripts/views/*');
require('scripts/router');
