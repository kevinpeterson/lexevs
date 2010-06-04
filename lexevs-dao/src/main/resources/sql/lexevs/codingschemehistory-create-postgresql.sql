--SQLWAYS_EVAL# 
--SQLWAYS_EVAL# Enterprise Architect Version 7.5.850
--SQLWAYS_EVAL# Tuesday, 01 June, 2010 
--SQLWAYS_EVAL# Oracle 
--SQLWAYS_EVAL# 




--SQLWAYS_EVAL# 
CREATE TABLE @PREFIX@h_associationEntity
(
   associationEntityGuid     VARCHAR(36) NOT NULL,
   entityGuid                VARCHAR(36) NOT NULL,
   forwardName               VARCHAR(100),    --SQLWAYS_EVAL# that the "from" entity plays with respect to the "to" entry.  Should be phrased in terms of the default language of the association and imply direction. 
   reverseName               VARCHAR(100),    --SQLWAYS_EVAL# should be represented when reading from target to source 
   isNavigable               BOOLEAN,    --SQLWAYS_EVAL# the reverse direction of the associaton is "navigable", meaning that it is makes sense to represent the target to source side of the association. 
   isTransitive              BOOLEAN,    --SQLWAYS_EVAL# association is transitive ( r(a,b), r(b,c) -> r(a,c)). False means not transitive. If absent, transitivity is unknown or not applicable. 
   isTranslationAssociation  BOOLEAN,    --SQLWAYS_EVAL# association set represents a translation mapping from source to the target. 
   entryStateGuid            VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_associationEntity IS 'SQLWAYS_EVAL# of a binary relation that can be asserted between two entities or an entity and a property. The entityType for the class concept must be "associationEntity".';
COMMENT ON COLUMN @PREFIX@h_associationEntity.forwardName               IS 'SQLWAYS_EVAL# that the "from" entity plays with respect to the "to" entry.  Should be phrased in terms of the default language of the association and imply direction.';
COMMENT ON COLUMN @PREFIX@h_associationEntity.reverseName               IS 'SQLWAYS_EVAL# should be represented when reading from target to source';
COMMENT ON COLUMN @PREFIX@h_associationEntity.isNavigable               IS 'SQLWAYS_EVAL# the reverse direction of the associaton is "navigable", meaning that it is makes sense to represent the target to source side of the association.';
COMMENT ON COLUMN @PREFIX@h_associationEntity.isTransitive              IS 'SQLWAYS_EVAL# association is transitive ( r(a,b), r(b,c) -> r(a,c)). False means not transitive. If absent, transitivity is unknown or not applicable.';
COMMENT ON COLUMN @PREFIX@h_associationEntity.isTranslationAssociation  IS 'SQLWAYS_EVAL# set represents a translation mapping from source to the target.';

CREATE TABLE @PREFIX@h_codingScheme
(
   codingSchemeGuid   VARCHAR(36) NOT NULL,
   codingSchemeName   VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# for the coding scheme within a particular context. Usually a mnemonic 
   codingSchemeURI    VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# URI 
   representsVersion  VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# currently represented by the coding scheme 
   formalName         VARCHAR(250),    --SQLWAYS_EVAL# of the coding scheme. 
   defaultLanguage    VARCHAR(32),    --SQLWAYS_EVAL# if not otherwise specified 
   approxNumConcepts  BIGINT,    --SQLWAYS_EVAL# system about the approximate number of concepts in the scheme (optional) 
   description        TEXT,
   copyright          TEXT,    --SQLWAYS_EVAL# (optional) 
   isActive           BOOLEAN DEFAULT TRUE,
   owner              VARCHAR(250),
   status             VARCHAR(50),
   effectiveDate      TIMESTAMP,
   expirationDate     TIMESTAMP,
   releaseGuid        VARCHAR(36),    --SQLWAYS_EVAL# unique name of a system release. 
   entryStateGuid     VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to entryState table. 
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_codingScheme.codingSchemeName   IS 'SQLWAYS_EVAL# for the coding scheme within a particular context. Usually a mnemonic';
COMMENT ON COLUMN @PREFIX@h_codingScheme.codingSchemeURI    IS 'SQLWAYS_EVAL# URI';
COMMENT ON COLUMN @PREFIX@h_codingScheme.representsVersion  IS 'SQLWAYS_EVAL# currently represented by the coding scheme';
COMMENT ON COLUMN @PREFIX@h_codingScheme.formalName         IS 'SQLWAYS_EVAL# the coding scheme.';
COMMENT ON COLUMN @PREFIX@h_codingScheme.defaultLanguage    IS 'SQLWAYS_EVAL# if not otherwise specified';
COMMENT ON COLUMN @PREFIX@h_codingScheme.approxNumConcepts  IS 'SQLWAYS_EVAL# about the approximate number of concepts in the scheme (optional)';
COMMENT ON COLUMN @PREFIX@h_codingScheme.copyright          IS 'SQLWAYS_EVAL# (optional)';
COMMENT ON COLUMN @PREFIX@h_codingScheme.releaseGuid        IS 'SQLWAYS_EVAL# name of a system release.';
COMMENT ON COLUMN @PREFIX@h_codingScheme.entryStateGuid     IS 'SQLWAYS_EVAL# entryState table.';

CREATE TABLE @PREFIX@h_csMultiAttrib
(
   csMultiAttribGuid  VARCHAR(36) NOT NULL,
   codingSchemeGuid   VARCHAR(36) NOT NULL,
   attributeType      VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# source 
   attributeValue     VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# and source. 
   subRef             VARCHAR(250),
   role               VARCHAR(250),
   entryStateGuid     VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_csMultiAttrib.attributeType      IS 'SQLWAYS_EVAL# source';
COMMENT ON COLUMN @PREFIX@h_csMultiAttrib.attributeValue     IS 'SQLWAYS_EVAL# source.';

CREATE TABLE @PREFIX@h_entity
(
   entityGuid           VARCHAR(36) NOT NULL,
   codingSchemeGuid     VARCHAR(36) NOT NULL,
   entityCode           VARCHAR(200) NOT NULL,    --SQLWAYS_EVAL# code within coding system 
   entityCodeNamespace  VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# of the namespace for the entrycode.  Defaults to the codingSchemeName of the containing codedEntry 
   isDefined            BOOLEAN,    --SQLWAYS_EVAL# the concept has at least one set of necessary and sufficient condition. 
   isAnonymous          BOOLEAN,    --SQLWAYS_EVAL# this node doesn't have an actual code in the code system 
   description          TEXT,
   isActive             BOOLEAN DEFAULT TRUE,
   owner                VARCHAR(250),
   status               VARCHAR(50),
   effectiveDate        TIMESTAMP,
   expirationDate       TIMESTAMP,
   entryStateGuid       VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_entity.entityCode           IS 'SQLWAYS_EVAL# within coding system';
COMMENT ON COLUMN @PREFIX@h_entity.entityCodeNamespace  IS 'SQLWAYS_EVAL# of the namespace for the entrycode.  Defaults to the codingSchemeName of the containing codedEntry';
COMMENT ON COLUMN @PREFIX@h_entity.isDefined            IS 'SQLWAYS_EVAL# the concept has at least one set of necessary and sufficient condition.';
COMMENT ON COLUMN @PREFIX@h_entity.isAnonymous          IS 'SQLWAYS_EVAL# this node doesn''t have an actual code in the code system';

CREATE TABLE @PREFIX@h_entityAssnQuals
(
   entityAssnQualsGuid  VARCHAR(36) NOT NULL,
   referenceGuid        VARCHAR(36) NOT NULL,
   qualifierName        VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# of a qualifier. Must be in supportedAssociationQualifier. 
   qualifierValue       VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# value 
   entryStateGuid       VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_entityAssnQuals.qualifierName        IS 'SQLWAYS_EVAL# of a qualifier. Must be in supportedAssociationQualifier.';
COMMENT ON COLUMN @PREFIX@h_entityAssnQuals.qualifierValue       IS 'SQLWAYS_EVAL# value';

CREATE TABLE @PREFIX@h_entityAssnsToData
(
   entityAssnsDataGuid        VARCHAR(36) NOT NULL,
   associationPredicateGuid   VARCHAR(36) NOT NULL,
   sourceEntityCode           VARCHAR(200) NOT NULL,
   sourceEntityCodeNamespace  VARCHAR(50) NOT NULL,
   associationInstanceId      VARCHAR(50),    --SQLWAYS_EVAL# uniqely names this entry within the context of the associationInstance. 
   isDefining                 BOOLEAN,
   isInferred                 BOOLEAN,
   dataValue                  TEXT,    --SQLWAYS_EVAL# value 
   isActive                   BOOLEAN,
   owner                      VARCHAR(250),
   status                     VARCHAR(50),
   effectiveDate              TIMESTAMP,
   expirationDate             TIMESTAMP,
   entryStateGuid             VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to entryState table. 
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_entityAssnsToData.associationInstanceId      IS 'SQLWAYS_EVAL# uniqely names this entry within the context of the associationInstance.';
COMMENT ON COLUMN @PREFIX@h_entityAssnsToData.dataValue                  IS 'SQLWAYS_EVAL# value';
COMMENT ON COLUMN @PREFIX@h_entityAssnsToData.entryStateGuid             IS 'SQLWAYS_EVAL# entryState table.';

CREATE TABLE @PREFIX@h_entityAssnsToEntity
(
   entityAssnsGuid            VARCHAR(36) NOT NULL,
   associationPredicateGuid   VARCHAR(36) NOT NULL,
   sourceEntityCode           VARCHAR(200) NOT NULL,
   sourceEntityCodeNamespace  VARCHAR(50) NOT NULL,
   targetEntityCode           VARCHAR(200) NOT NULL,
   targetEntityCodeNamespace  VARCHAR(50) NOT NULL,
   associationInstanceId      VARCHAR(50),    --SQLWAYS_EVAL# to identify association instance.  
   isDefining                 BOOLEAN,
   isInferred                 BOOLEAN,
   isActive                   BOOLEAN,
   owner                      VARCHAR(250),
   status                     VARCHAR(50),
   effectiveDate              TIMESTAMP,
   expirationDate             TIMESTAMP,
   entryStateGuid             VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to entryState table. 
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_entityAssnsToEntity.associationInstanceId      IS 'SQLWAYS_EVAL# identify association instance. ';
COMMENT ON COLUMN @PREFIX@h_entityAssnsToEntity.entryStateGuid             IS 'SQLWAYS_EVAL# entryState table.';

CREATE TABLE @PREFIX@h_property
(
   propertyGuid          VARCHAR(36) NOT NULL,
   referenceGuid         VARCHAR(36) NOT NULL,
   referenceType         VARCHAR(50) NOT NULL,
   propertyId            VARCHAR(50),    --SQLWAYS_EVAL# of the property within the context of the coded entry 
   propertyType          VARCHAR(15),    --SQLWAYS_EVAL# the objects stored in this table.  Can be - 'presentation', 'definition', 'comment', 'instruction', 'property' 
   propertyName          VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# or tag of the property. Must be in supportedProperty 
   language              VARCHAR(32),    --SQLWAYS_EVAL# spoken language of the property. Must be in supportedLanguage. Default- defaultLanguage for coding Scheme 
   format                VARCHAR(50),
   isPreferred           BOOLEAN,    --SQLWAYS_EVAL# *if* the text meets the selection criteria, it should be the preferred form. 
   matchIfNoContext      BOOLEAN,    --SQLWAYS_EVAL# entry matches if no context is supplied 
   degreeOfFidelity      VARCHAR(50),    --SQLWAYS_EVAL# term approximates the meaning of a concept 
   representationalForm  VARCHAR(50),    --SQLWAYS_EVAL# represents the concept (abbrev, acronym, etc.) - Must be in supportedRepresentationalForm 
   propertyValue         TEXT NOT NULL,    --SQLWAYS_EVAL# and associated value that further identifies or describes the intent of the entity code. 
   isActive              BOOLEAN,
   owner                 VARCHAR(250),
   status                VARCHAR(50),
   effectiveDate         TIMESTAMP,
   expirationDate        TIMESTAMP,
   entryStateGuid        VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_property.propertyId            IS 'SQLWAYS_EVAL# of the property within the context of the coded entry';
COMMENT ON COLUMN @PREFIX@h_property.propertyType          IS 'SQLWAYS_EVAL# the objects stored in this table.  Can be - ''presentation'', ''definition'', ''comment'', ''instruction'', ''property''';
COMMENT ON COLUMN @PREFIX@h_property.propertyName          IS 'SQLWAYS_EVAL# or tag of the property. Must be in supportedProperty';
COMMENT ON COLUMN @PREFIX@h_property.language              IS 'SQLWAYS_EVAL# spoken language of the property. Must be in supportedLanguage. Default- defaultLanguage for coding Scheme';
COMMENT ON COLUMN @PREFIX@h_property.isPreferred           IS 'SQLWAYS_EVAL# *if* the text meets the selection criteria, it should be the preferred form.';
COMMENT ON COLUMN @PREFIX@h_property.matchIfNoContext      IS 'SQLWAYS_EVAL# entry matches if no context is supplied';
COMMENT ON COLUMN @PREFIX@h_property.degreeOfFidelity      IS 'SQLWAYS_EVAL# approximates the meaning of a concept';
COMMENT ON COLUMN @PREFIX@h_property.representationalForm  IS 'SQLWAYS_EVAL# the concept (abbrev, acronym, etc.) - Must be in supportedRepresentationalForm';
COMMENT ON COLUMN @PREFIX@h_property.propertyValue         IS 'SQLWAYS_EVAL# and associated value that further identifies or describes the intent of the entity code.';

CREATE TABLE @PREFIX@h_propertyLinks
(
   propertyLinksGuid   VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for a given property link. 
   sourcePropertyGuid  VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# the source property guid. (The identifier of the first property in the link.) 
   link                VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# of the type of link between properties.  (Examples include acronymFor, abbreviationOf, spellingVariantOf, etc. Must be in supportedPropertyLink) propertyLink must match a local id of a supportedPropertyLink in the corresponding mapping section. 
   targetPropertyGuid  VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# the target property guid. (The identifier of the second property in the link.) 
   entryStateGuid      VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_propertyLinks IS 'SQLWAYS_EVAL# link between two properties.';
COMMENT ON COLUMN @PREFIX@h_propertyLinks.propertyLinksGuid   IS 'SQLWAYS_EVAL# for a given property link.';
COMMENT ON COLUMN @PREFIX@h_propertyLinks.sourcePropertyGuid  IS 'SQLWAYS_EVAL# the source property guid. (The identifier of the first property in the link.)';
COMMENT ON COLUMN @PREFIX@h_propertyLinks.link                IS 'SQLWAYS_EVAL# of the type of link between properties.  (Examples include acronymFor, abbreviationOf, spellingVariantOf, etc. Must be in supportedPropertyLink) propertyLink must match a local id of a supportedPropertyLink in the corresponding mapping section.';
COMMENT ON COLUMN @PREFIX@h_propertyLinks.targetPropertyGuid  IS 'SQLWAYS_EVAL# the target property guid. (The identifier of the second property in the link.)';

CREATE TABLE @PREFIX@h_propertyMultiAttrib
(
   propMultiAttribGuid  VARCHAR(36) NOT NULL,
   propertyGuid         VARCHAR(36) NOT NULL,
   attributeType        VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# qualifier 
   attributeId          VARCHAR(50),    --  attributeId 
   attributeValue       VARCHAR(250),    --SQLWAYS_EVAL# or qualifier name - must come from a SupportedXXX value 
   subRef               VARCHAR(250),
   role                 VARCHAR(250),
   entryStateGuid       VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_propertyMultiAttrib.attributeType        IS 'SQLWAYS_EVAL# qualifier';
COMMENT ON COLUMN @PREFIX@h_propertyMultiAttrib.attributeId          IS 'attributeId';
COMMENT ON COLUMN @PREFIX@h_propertyMultiAttrib.attributeValue       IS 'SQLWAYS_EVAL# or qualifier name - must come from a SupportedXXX value';

CREATE TABLE @PREFIX@h_relation
(
   relationGuid               VARCHAR(36) NOT NULL,
   codingSchemeGuid           VARCHAR(36) NOT NULL,
   containerName              VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# collection of relations. Must be in supportedContainerName. 
   isMapping                  BOOLEAN,    --SQLWAYS_EVAL# that this set of associations represents a mapping between two terminologies if value is true. 
   sourceCodingScheme         VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme. 
   sourceCodingSchemeVersion  VARCHAR(50),
   targetCodingScheme         VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default target codingScheme. 
   targetCodingSchemeVersion  VARCHAR(50),
   description                TEXT,
   isActive                   BOOLEAN,
   owner                      VARCHAR(250),
   status                     VARCHAR(50),
   effectiveDate              TIMESTAMP,
   expirationDate             TIMESTAMP,
   entryStateGuid             VARCHAR(36) NOT NULL
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@h_relation.containerName              IS 'SQLWAYS_EVAL# collection of relations. Must be in supportedContainerName.';
COMMENT ON COLUMN @PREFIX@h_relation.isMapping                  IS 'SQLWAYS_EVAL# this set of associations represents a mapping between two terminologies if value is true.';
COMMENT ON COLUMN @PREFIX@h_relation.sourceCodingScheme         IS 'SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme.';
COMMENT ON COLUMN @PREFIX@h_relation.targetCodingScheme         IS 'SQLWAYS_EVAL# in associationInstance, this is the default target codingScheme.';


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@h_associationEntity ADD CONSTRAINT PK_H_ASSOCIATIONENTITY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_codingScheme ADD CONSTRAINT PK_H_CODINGSCHEME 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_entity ADD CONSTRAINT PK_H_ENTITY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_entityAssnsToData ADD CONSTRAINT PK_H_ENTITYASSNSTODATA 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_entityAssnsToEntity ADD CONSTRAINT PK_H_ENTITYASSNSTOENTITY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_property ADD CONSTRAINT PK_H_PROPERTY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_propertyLinks ADD CONSTRAINT PK_H_PROPERTYLINKS 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_relation ADD CONSTRAINT PK_H_RELATION 
PRIMARY KEY(entryStateGuid);


--SQLWAYS_EVAL# 
CREATE INDEX IDX_H_CODINGSCHEMEURI ON @PREFIX@h_codingScheme
(codingSchemeURI);

CREATE INDEX IDX_H_CSMULTIATTRIB ON @PREFIX@h_csMultiAttrib
(codingSchemeGuid, 
attributeType);

CREATE INDEX IDX_H_ENTITY ON @PREFIX@h_entity
(codingSchemeGuid, 
entityCode);

CREATE INDEX IDX_H_ENTITYNS ON @PREFIX@h_entity
(codingSchemeGuid, 
entityCode, 
entityCodeNamespace);

CREATE INDEX IDX_H_ENTASTODATA_SOURCE ON @PREFIX@h_entityAssnsToData
(associationPredicateGuid, 
sourceEntityCode);

CREATE INDEX IDX_H_ENTASTOENT_SOURCE ON @PREFIX@h_entityAssnsToEntity
(associationPredicateGuid, 
sourceEntityCode);

CREATE INDEX IDX_H_ENTASTOENT_SOURCENS ON @PREFIX@h_entityAssnsToEntity
(associationPredicateGuid, 
sourceEntityCode, 
sourceEntityCodeNamespace);

CREATE INDEX IDX_H_ENTASTOENT_TARGET ON @PREFIX@h_entityAssnsToEntity
(associationPredicateGuid, 
targetEntityCode);

CREATE INDEX IDX_H_ENTASTOENT_TARGETNS ON @PREFIX@h_entityAssnsToEntity
(associationPredicateGuid, 
targetEntityCode, 
targetEntityCodeNamespace);

CREATE INDEX IDX_H_REFERENCEGUID ON @PREFIX@h_property
(referenceGuid);

CREATE INDEX IDX_H_SOURCEPROPERTYGUID ON @PREFIX@h_propertyLinks
(sourcePropertyGuid);

CREATE INDEX IDX_H_TARGETPROPERTYGUID ON @PREFIX@h_propertyLinks
(targetPropertyGuid);

CREATE INDEX IDX_H_PROPERTYMULTIATTRIB ON @PREFIX@h_propertyMultiAttrib
(propertyGuid);


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@h_codingScheme ADD CONSTRAINT FK_H_CODINGSCHEME_CODINGSCHEME 
FOREIGN KEY(codingSchemeGuid) REFERENCES @PREFIX@codingScheme(codingSchemeGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_csMultiAttrib ADD CONSTRAINT FK_H_CSMULTIATTR_CSMULTIATTRIB 
FOREIGN KEY(csMultiAttribGuid) REFERENCES @PREFIX@csMultiAttrib(csMultiAttribGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_entity ADD CONSTRAINT FK_H_ENTITY_ENTITY 
FOREIGN KEY(entityGuid) REFERENCES @PREFIX@entity(entityGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_entityAssnQuals ADD CONSTRAINT FK_H_ENTITYASSNQ_ENTITYASSNQUA 
FOREIGN KEY(entityAssnQualsGuid) REFERENCES @PREFIX@entityAssnQuals(entityAssnQualsGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_entityAssnsToData ADD CONSTRAINT FK_H_ENTITYASSNSD_ENTITYASSNSD 
FOREIGN KEY(entityAssnsDataGuid) REFERENCES @PREFIX@entityAssnsToData(entityAssnsDataGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_entityAssnsToEntity ADD CONSTRAINT FK_H_ENTITYASSNSE_ENTITYASSNSE 
FOREIGN KEY(entityAssnsGuid) REFERENCES @PREFIX@entityAssnsToEntity(entityAssnsGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_property ADD CONSTRAINT FK_H_PROPERTY_PROPERTY 
FOREIGN KEY(propertyGuid) REFERENCES @PREFIX@property(propertyGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_propertyLinks ADD CONSTRAINT FK_H_PROPERTYLIN_PROPERTYLINKS 
FOREIGN KEY(propertyLinksGuid) REFERENCES @PREFIX@propertyLinks(propertyLinksGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_propertyMultiAttrib ADD CONSTRAINT FK_H_PROPERTYMUL_PROPERTYMULTI 
FOREIGN KEY(propMultiAttribGuid) REFERENCES @PREFIX@propertyMultiAttrib(propMultiAttribGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_relation ADD CONSTRAINT FK_H_RELATION_RELATION 
FOREIGN KEY(relationGuid) REFERENCES @PREFIX@relation(relationGuid)
ON DELETE CASCADE;