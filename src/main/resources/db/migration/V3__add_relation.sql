ALTER TABLE wichtel
  ADD COLUMN event_id uuid;

ALTER TABLE wichtel
  ADD CONSTRAINT wichtel_to_wichtel_event_fk FOREIGN KEY (event_id) REFERENCES wichtel_event (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_wichtel_to_wichtel_event_fk
  ON wichtel(event_id);
