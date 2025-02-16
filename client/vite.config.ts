import { VitePWA } from 'vite-plugin-pwa';
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/

export default defineConfig(({ mode }) => {
  const isProduction = mode === 'production';
  console.log(isProduction);

  return {
    plugins: [
      react(),
      VitePWA({
        registerType: 'prompt',
        injectRegister: false,

        pwaAssets: {
          disabled: false,
          config: true,
        },

        manifest: {
          name: 'pull-up',
          short_name: 'pup',
          description: 'pull-up',
          theme_color: '#ffffff',
        },

        workbox: {
          globPatterns: ['**/*.{js,css,html,svg,png,ico}'],
          cleanupOutdatedCaches: true,
          clientsClaim: true,
        },

        devOptions: {
          enabled: false,
          navigateFallback: 'index.html',
          suppressWarnings: true,
          type: 'module',
        },
      }),
    ],
    resolve: {
      alias: {
        '@': '/src',
      },
    },
    define: {
      global: 'window',
    },
    server: {
      host: '0.0.0.0',
      port: 5173,
      allowedHosts: ['www.pull-up.store'],
    },
  };
});
