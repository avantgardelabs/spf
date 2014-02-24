Autor: Eric Engstfeld

Fecha:	18 de Febrero, 2014
 
#Servicio de Gestión Documental
Servicio encargado de la gestión de documentos utilizando un gestor documental basado en el standard CMIS.

<span id="0"/></span>

#Índice
1.  [Objetivos del documento](#1)
2.  [Introducción](#2)
3.  [Tecnología](#3)
4.  [Arquitectura](#4)
      * [General](#41)
      * [Módulos](#42)
        - [DocumentManagerService](#421)
        - [DocumentManagerServiceImpl](#422)
        - [CmisConnector](#423)
        - [DocumentManagerRestService](#423)
5. [Compilación, Instalación y Ejecución](#5)
      * [Software necesario](#51)
      * [Pasos](#52)
      	- [Descarga](#521)
      	- [Compilación](#522)
      	- [Instalación](#523)
      * [Configuración](#53)
      * [Ejecución](#54)
6. [Uso](#6)
      * [Métodos Expuestos](#61)
        - [update](#611)
        - [upload](#612)
        - [get](#613)
        - [delete](#614)
7. [Mantenimiento](#7)

<span id="1"/></span>
##1. Objectivos del documento
Exponer los aspectos técnicos y generales de la aplicación, su arquitectura, así también como sus distintos modulos, explicando cada uno de ellos para transmitir luego su forma de uso e instalación. Por último se pretende también dar información detallada sobre su mantenimiento a lo largo del tiempo.

<span id="2"/></span>
##2. Introducción
El propósito del Servicio de Gestión Documental es poder gestionar y administrar documentos de forma desatendida utilizando funciones básicas como crear, modificar, eliminar o recuperar, persistiéndolos siempre en un Gestor Documental basado en el standard CMIS, como por ejemplo [Alfresco](http://www.alfresco.com). Para resumir, podríamos decir entonces que el Servicio de Gestión Documental es un "facility" o interface entre el Gestor Documental CMIS y el resto del sistema que se comunica con él por medio de un servicio REST que expone las dichas funciones básicas detalladas más adelante.

<span id="3"/></span>
##3. Tecnología
El servicio está escrito en Java6 utilizando las siguientes tecnologías para su construcción:
- [Spring framework](http://springsource.org/spring-framework)
- [Apache maven](http://maven.apache.org/)
- [Apache commons](http://commons.apache.org/)
- [Apache chemistry](http://chemistry.apache.org/)
- [JAX-RS](https://jax-rs-spec.java.net/)
- [CORS filter](http://software.dzhuvinov.com/cors-filter.html)
- [SLF4J API Module](http://www.slf4j.org/)
- [Jackson](http://jackson.codehaus.org/)

<span id="4"/></span>
##4. Arquitectura

<span id="41"/></span>
##4.1 General
Esta compuesto por cuatro módulos bien definidos: DocumentManagerService, DocumentManagerServiceImpl, CmisConnector y DocumentManagerRestService.

<span id="42"/></span>

##4.2 Módulos

<span id="421"/></span>

###4.2.1 DocumentManagerService
Es el core del servicio: contiene la interface principal encargada de la comunicación entre el servicio Rest y el CmisConnector. Para su implementación se pueden utilizar una serie de helpers incluidos en este mismo módulo.
TODO agregar especificación de los métodos

<span id="422"/></span>

###4.2.2 DocumentManagerServiceImpl
Contiene la implementación de la interface principal del core encargada de la lógica de conexión entre el CmisConnector y el Servicio. 

<span id="423"/></span>

###4.2.3 CmisConnector
Es la API que soluciona la conexión con el Gestor Documental CMIS conteniendo una serie de métodos de utilización frecuente a la hora de llamar al Gestor. Utiliza principalmente para este fin la librería Apache chemistry mencionada anteriormente en la parte de [Tecnologías](#3).

<span id="424"/></span>

###4.2.4 DocumentManagerRestService
Servicio REST encargado de exponer la interface del core al mundo exterior. También se encarga del manejo de recibir/validar los parámetros recibidos y devolver correctamente la respuesta en formato [Json](http://www.json.org/).

Si bien estos métodos serán detallados más adelante en la sección de [Uso](#6) e aquí un pequeño resumen de los mismos:
- get: recupera un documento 
- upload: crea un documento
- update: modifica un documento
- delete: elimina un documento

<span id="5"/></span>

##5. Compilación, Instalación y Ejecución

<span id="51"/></span>

###5.1 Software necesario
La siguiente lista de software es necesaria para realizar la compilación e instalación del servicio:
- JDK 1.6.x ([Gu&iacute;a de instalaci&oacute;n](https://help.ubuntu.com/community/Java))
- Jboss-as-7.1.0.Final ([Descarga](http://www.jboss.org/jbossas/downloads/))
- Maven 3.0.4 ([Descarga](http://maven.apache.org/download.cgi),[Instalaci&oacute;n](http://maven.apache.org/download.cgi#Unix-based_Operating_Systems_Linux_Solaris_and_Mac_OS_X))
- Git (Solo para entorno de desarrollo, [Gu&iacute;a de instalaci&oacute;n](https://help.ubuntu.com/community/Git))

<span id="52"/></span>

###5.2 Pasos

<span id="521"/></span>

####5.2.1 Descarga
Esta disponible para su libre uso y descarga en [git](https://github.com/avantgardelabs/spf/tree/master/ServicioGestionDocumental). Para realizarla, ejecutar el siguiente comando en el directorio donde se desea compilar el código fuente:
& git clone https://github.com/avantgardelabs/spf.git

<span id="522"/></span>

####5.2.2 Compilación
Para compilar correctamente el servicio debemos primero instalar localmente usando maven los módulos previamente mencionados dentro del repositorio descargado. Entonces entraremos usando una terminal a cada uno de los componentes descargados y dentro del directorio principal que contiene el pom.xml escribiremos el siguiente comando:
& mvn install

<span id="523"/></span>

####5.2.3 Instalación
Luego de finalizar la compilación, entrar dentro de DocumentManagerRestService y editar las siguientes propiedades del archivo pom.xml:

<project>
	...
	<properties>
		<cmis.username>USUARIO DEL GESTOR DOCUMENTAL CMIS</cmis.username>
		<cmis.password>PASSWORD DEL GESTOR DOCUMENTAL CMIS</cmis.password>
		<cmis.repoId>REPOID DEL GESTOR DOCUMENTAL CMIS</cmis.repoId>
		<cmis.atomPubUrl>ATOMPUB URL DEL GESTOR DOCUMENTAL CMIS</cmis.atomPubUrl>
		<cmis.objects.id.prefix>PREFIJO DEL ID</cmis.objects.id.prefix>

		...	
	</properties>
	...
</project>

El repoId y la URL del servicio AtomPub debe proveerla el Gestor Documental que se este utilizando. En el caso de Alfresco 4.2.0 se podrán por ejemplo obtener esos datos desde la siguiente ruta: http://HOST:PORT/alfresco/service/cmis/index.html

Con la terminal dentro de ese mismo directorio, escribir el siguiente comando de maven para su empaquetamiento:
& mvn clean package

Tomar luego dentro de la carpeta target generada en consecuencia al comando el archivo DocumentManagerRestService-${VERSION}.war y moverlo/copiarlo al jboss en el directorio donde se realiza el deployment, por ejemplo:
& cp target/DocumentManagerRestService-${VERSION}.war $JBOSS_HOME/standalone/deployments

<span id="53"/></span>

###5.3 Configuración
Una vez deployado e instalado el Servicio, podremos re-configurarlo modificando los archivos a continuación:

	- ..WEB-INF/classes/config.properties: Contiene las configuraciones generales de conexión con el gestor CMIS:
		* nombre de usuario del gestor CMIS ->	cmis.username=admin
		* contraseña del gestor CMIS ->			cmis.password=admin
		* ID del repositorio del gestor CMIS ->	cmis.repoId=25068bb8-dd84-41c3-aa2f-64c9f3a0e53f
		* URL del servicio AtomPub expuesto ->	cmis.atomPubUrl=http://localhost:8081/alfresco/cmisatom
		* Prefijo de los IDs del gestor CMIS ->	cmis.objects.id.prefix=workspace://SpacesStore/ 
	
	- ..WEB-INF/classes/log4j.properties: Contiene las configuraciones asociadas al logging:

		* Root logger ->	log4j.rootLogger=INFO, stdout
							log4j.logger.bo.gob.aduana.sga.connector.cmis=DEBUG 

		* Main appender ->	log4j.appender.stdout=org.apache.log4j.ConsoleAppender
							log4j.appender.stdout.Target=System.out
							log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
							log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
	
	- ..WEB-INF/web.xml: Contiene las configuraciones asociadas al manejo de servlets, listeners, filters, etc.
	- ..WEB-INF/dispatcher-servlet.xml: Contiene las configuraciones de Spring generales.
	- ..WEB-INF/classes/application-context.xml: Contiene las configuraciones de Spring específicas del Servicio.
	
<span id="54"/></span>

###5.4 Ejecución
Para ejecutar el servicio bastaria con ejecutar correctamente el JBoss donde esté instalado. Referirse [aquí](http://docs.jboss.org/jbossas/guides/installguide/r1/en/html/start-stop.html) para encontrar información al respecto.

<span id="6"/></span>

##6. Uso

<span id="61"/></span>

###6.1 Métodos expuestos
Todos los métodos están expuestos a partir de "http://HOST:PORT/DocumentManagerRestService/rest", por lo tanto a continuación se nombrará sólo el sufijo correspondiente a cada método.

<span id="611"/></span>

####6.1.1 Update
Punto de vista funcional: modifica el contenido de un documento almacenado en el gestor documental a una versión posterior. Recibe como parámetro de tipo form-data el documento (File), el ID correspondiente al documento a modificar del servicio CMIS (String), el comentario de commit (String) y un boolean indicando si la modificación merece una version mayor o una menor (si se pasa de 1.0 a 2.0 por ejemplo o a 1.1).
Punto de vista técnico:
	-	Tipo de método POST, expuesto en "/update". Por lo que la url final sería "http://HOST:PORT/DocumentManagerRestService-${VERSION}/update"
	-	Parámetros de tipo form-data (key/type):
		-	document / File
		-	cmisId / String
		-	commitComment / String
		-	major / boolean
	-	Respuesta en formato JSon con la siguiente estructura:

<span id="612"/></span>

####6.1.2 Upload
Punto de vista funcional: carga un nuevo documento al gestor documental comenzando en la versión 1.0. Recibe como parámetro de tipo form-data el documento (File) y el ID correspondiente al folder donde se subirá (String).
Punto de vista técnico:
	-	Tipo de método POST, expuesto en "/upload". Por lo que la url final sería "http://HOST:PORT/DocumentManagerRestService-${VERSION}/upload"
	-	Parámetros de tipo form-data (key/type):
		-	document / File
		-	folderid / String
	-	Respuesta en formato JSon con la siguiente estructura:
	
<span id="613"/></span>

####6.1.3 Get
Punto de vista funcional: recupera un documento del gestor documental. Recibe como parámetro el ID correspondiente al documento del servicio CMIS y, opcional, la versión ya que por defecto retorna la última.
Punto de vista técnico:
	-	Tipo de método GET expuesto en "/get". Por lo que la url final sería "http://HOST:PORT/DocumentManagerRestService-${VERSION}/get?cmisId=${cmisId}&version=${version}"
	-	Respuesta en formato JSon con la siguiente estructura:

<span id="614"/></span>

####6.1.4 Delete
Punto de vista funcional: elimina un documento del gestor documental. Recibe como parámetro el ID correspondiente al documento del servicio CMIS y, opcional, un booleano allVersions indicando si se debe eliminar todas las versiones ya que por defecto elimina sólo la última.
Punto de vista técnico:
	-	Tipo de método GET expuesto en "/delete". Por lo que la url final sería "http://HOST:PORT/DocumentManagerRestService-${VERSION}/delete?cmisId=${cmisId}&allVersions=${version}"
	-	Respuesta en formato JSon con la siguiente estructura:

<span id="7"/></span>

##7. Mantenimiento
La idea del 