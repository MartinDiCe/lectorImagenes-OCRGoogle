# Setup inicial
Para poder ejecutar el código del proyecto debes estar autenticado contra Google Cloud API (ver ref. ); como el proyecto ya se encuentra configurado en Google Cloud sólo realiza los siguiente pasos:

- [Descarga e instala Google Cloud Vision CLI](https://cloud.google.com/sdk/docs/install?hl=es-419)
- desde consola de comandos (CMD en Win) inicializa gcloud CLI `gcloud init` (ref. https://cloud.google.com/vision/docs/setup?hl=es-419#sdk ): se abre una solapa del navegador con el login form; pedile las credenciales de la cuenta que usamos a tu PM 
  - como parte del login inicial en la consola te pide seleccionar el proyecto, elige el **[4] telegramocr**
- genera el archivo de credenciales correspondiente: `gcloud auth application-default login` (ref. https://cloud.google.com/vision/docs/setup?hl=es-419#client-library-user-account-authentication )
- ya estas listo para correr tu código


