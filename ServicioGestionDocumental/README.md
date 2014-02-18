Autor: Eric Engstfeld

Fecha:	18 de Febrero, 2014
 
# Servicio Gestion Documental
Servicio encargado de la gestion de documentos utilizando un gestor documental basado en el standard CMIS (pj. Alfresco).

<span id="0"/></span>

Indice
1.  [Objetivos del documento](#1)
2.  [Introduccion](#2)
3.  [Arquitectura](#3)
      * [General](#31)
      * [Modulos](#32)
        - [DocumentManagerService](#321)
        - [DocumentManagerServiceImpl](#322)
        - [DocumentManagerRestService](#323)
4. [Compilacion, Instalacion y Ejecucion](#4)
      * [Software necesario](#41)
      * [Pasos](#42)
      * [Configuracion](#43)
      * [Ejecucion](#44)
5. [Uso](#5)
      * [Metodos Expuestos](#51)
        - [update](#511)
        - [upload](#512)
        - [get](#513)
        - [delete](#514)
6. [Mantenimiento](#6)

## 1. Objectivos del documento
Exponer los aspectos tecnicos y generales de la aplicacion, su arquitectura, asi tambien como sus distintos modulos, explicando cada uno de ellos para transmitir luego su forma de uso e instalacion. Por ultimo se pretende tambien dar informacion detallada sobre su mantenimiento a lo largo del tiempo.

## 2. Introduccion
El proposito del Serivicio Gestion Documental es poder gestionar y administrar documentos de forma desatendida utilizando funciones basicas como crear, modificar, eliminar o recuperar, persistiendolos siempre en un Gestor Documental basado en el estandard CMIS, como por ejemplo Alfresco (http://www.alfresco.com). Para resumir, podriamos decir entonces que el Servicio Gestion Documental es un "facility" o interface entre el Gestor Documental CMIS y el resto del sistema que se comunica por medio de un servicio REST que expone las dichas funciones basicas detalladas mas adelante.

## 3. Arquitectura
Esta compuesto por tres modulos bien definidos: DocumentManagerService, DocumentManagerServiceImpl y DocumentManagerRestService.
