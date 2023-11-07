# Setup inicial
Para poder ejecutar el código del proyecto debes estar autenticado contra Google Cloud API (ver ref. ); como el proyecto ya se encuentra configurado en Google Cloud sólo realiza los siguiente pasos:

- [Descarga e instala Google Cloud Vision CLI](https://cloud.google.com/sdk/docs/install?hl=es-419)
- Desde consola de comandos (CMD en Win) inicializa gcloud CLI `gcloud init` (ref. https://cloud.google.com/vision/docs/setup?hl=es-419#sdk ): se abre una solapa del navegador con el login form; pedile las credenciales de la cuenta que usamos a tu PM 

⚠️ **LAS CREDENCIALES DE TESTEO SE ENCUENTRAN FIJADAS EN EL GRUPO DE MD DE DISCORD**

- Como parte del login inicial en la consola te pide seleccionar el proyecto, elige el **[4] telegramocr**
- Genera el archivo de credenciales correspondiente: `gcloud auth application-default login` (ref. https://cloud.google.com/vision/docs/setup?hl=es-419#client-library-user-account-authentication )
- Ya estas listo para correr tu código

**FUNCIONALIDADES**

- Descarga de Imagen con extensión TIF
- Lector de Imagen con la api del OCR de Google (TextReader.java)
- Procesamiento de los textos devueltos (Ordenamiento, exportación JSON, Procesamiento y trabajo sobre el JSON vectorizado)
- Concatenación de los textos caracteres con hasta 4 para unificarlos en uno si estan en el mismo eje Y (TextConcatenator.java)
- Sumar vertices aproximados para encontrar las palabras dinamica a traves de palabras claves. (VertexSUM.Java)
  * Este vertexSum requiere parametrizar la diferencia aproximada de la palabra clave fija y la dinamica, solo funciona para leer los mismo tipo de imagen.
  * Requiere ajustar los parametros en SumValueConfig.java.
  * Esta función busca una palabra clave, que tenga cercana a una segunda palabra clave, y obtiene sus vertices si es la que buscamos.
- Calculando la suma de los vertices, va y busca el vertice que se acerque más al aproximado que sacamos con VertexFinder.java
- Busca el texto más cercano después de obtener los vertices de la palabra clave fija con la clase textSearcher.java, dando prioridad a número para nuestros ejemplos.
- Convierte los textos encontrados en número y en caso de obtener strings vamos conrtiendo a posibles int con StringToNumberConverter.java.
- Devuelve un JSON con la información obtenida, teniendo en cuenta que devolvemos INT y si devuelve algún string o dato que no se puede parsear, se devuelve un -1.

**DATOS A TENER EN CUENTA**
- En caso de usar el código para otro tipo de imagen, habría que tocar las clases, solo esta pensado para obtener números de las imagenes
- Esta pensando para un tipo de imagen, por lo que no es generico
- Si se utiliza, tengan en cuenta ajustar las clases mencionadas en Funcionalidades a gusto para obtener y armar los datos que necesiten.


*Proyecto realizado por MartinDice y Agus5534*



