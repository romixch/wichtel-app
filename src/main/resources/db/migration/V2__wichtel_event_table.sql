-- Table: "wichtel_event"

-- DROP TABLE "wichtel_event";

CREATE TABLE "wichtel_event"
(
  "id" uuid NOT NULL,
  "name" character varying(100) NOT NULL,
  "completed" boolean,
  CONSTRAINT wichtel_event_pk PRIMARY KEY ("id")
)
WITH (
  OIDS=FALSE
);
