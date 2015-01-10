-- Table: "wichtel"

-- DROP TABLE "wichtel";

CREATE TABLE "wichtel"
(
  "id" uuid NOT NULL,
  "name" character varying(50) NOT NULL,
  "email" character varying(200) NOT NULL,
  "wichtel_to_id" uuid,
  "mail_sent" boolean,
  "send_error" text,
  CONSTRAINT pk PRIMARY KEY ("id")
)
WITH (
  OIDS=FALSE
);
