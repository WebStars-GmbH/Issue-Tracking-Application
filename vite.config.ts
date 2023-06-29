import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';

const customConfig: UserConfigFn = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
    server: {
        host: '0.0.0.0',
        strictPort: true,
    }
});

export default overrideVaadinConfig(customConfig);
