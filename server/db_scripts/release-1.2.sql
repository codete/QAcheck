ALTER TABLE COMPARISON_SETTINGS ADD COLUMN ALLOWED_DELTA NUMBER NOT NULL DEFAULT(0.0);
ALTER TABLE COMPARISON_SETTINGS ADD COLUMN HORIZONTAL_SHIFT INT NOT NULL DEFAULT(0);
ALTER TABLE COMPARISON_SETTINGS ADD COLUMN VERTICAL_SHIFT INT NOT NULL DEFAULT(0);
ALTER TABLE COMPARISON_SETTINGS ADD COLUMN SHOW_DETECTED_SHIFT BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE COMPARISON_SETTINGS ADD COLUMN PERCEPTUAL_MODE BOOLEAN NOT NULL DEFAULT(FALSE);