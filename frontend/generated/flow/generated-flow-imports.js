import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/app-layout/theme/lumo/vaadin-drawer-toggle.js';
import '@vaadin/vaadin-lumo-styles/utility-global.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '06ae39ad3795561632d1701923b9280aab0d84196dba10f75bc9cd427bdb6aab') {
    pending.push(import('./chunks/chunk-06ae39ad3795561632d1701923b9280aab0d84196dba10f75bc9cd427bdb6aab.js'));
  }
  if (key === 'ead70e78e057381f84be264d307d37cfc2fb35b524c94e1569dfb94e7ae5138e') {
    pending.push(import('./chunks/chunk-ead70e78e057381f84be264d307d37cfc2fb35b524c94e1569dfb94e7ae5138e.js'));
  }
  if (key === '649e3c825d4c04d37047c773b751f85efa81f9cb318c27cc03b6983c308b315a') {
    pending.push(import('./chunks/chunk-649e3c825d4c04d37047c773b751f85efa81f9cb318c27cc03b6983c308b315a.js'));
  }
  if (key === 'f58ef2ba80d8f627ee8f734320c90a2f00a3662af2c0e761006a95cf6651d542') {
    pending.push(import('./chunks/chunk-f58ef2ba80d8f627ee8f734320c90a2f00a3662af2c0e761006a95cf6651d542.js'));
  }
  if (key === '639619edc833c8733c7691e521970c92ecc1fcce724bf3c8c5a58ed16c5b36bc') {
    pending.push(import('./chunks/chunk-639619edc833c8733c7691e521970c92ecc1fcce724bf3c8c5a58ed16c5b36bc.js'));
  }
  if (key === 'b3ee82e62fe095cd3dcb6fcd65a4e21469444e3459ec6b4487f6fffce7d6c941') {
    pending.push(import('./chunks/chunk-b3ee82e62fe095cd3dcb6fcd65a4e21469444e3459ec6b4487f6fffce7d6c941.js'));
  }
  if (key === '345bd7e7b5c18c8f70002f6396e59c82b05c003d8cb7d28ff60db2fdfd4b2cc1') {
    pending.push(import('./chunks/chunk-345bd7e7b5c18c8f70002f6396e59c82b05c003d8cb7d28ff60db2fdfd4b2cc1.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;