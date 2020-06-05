import {KeycloakService} from 'keycloak-angular';
import {environment} from '../../../environments/environment';

export function initializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      const { keycloakConfig } = environment;
      try {
        await keycloak.init({
          config: keycloakConfig,
          initOptions: {
            onLoad: 'check-sso',
            checkLoginIframe: false
          },
          bearerExcludedUrls: ['/signup']
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
