DROP TABLE IF EXISTS ClientAccess CASCADE;
DROP TABLE IF EXISTS LogFarm CASCADE;
DROP TABLE IF EXISTS LogError CASCADE;
DROP TABLE IF EXISTS ReadAccess CASCADE;
DROP TABLE IF EXISTS DiskSmart CASCADE;
DROP TABLE IF EXISTS Disk CASCADE;
DROP TABLE IF EXISTS HarvesterNode CASCADE;

CREATE TABLE HarvesterNode (
  id BIGSERIAL,
  uid VARCHAR(100) NOT NULL, -- send by client
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- actual time by client
  ip VARCHAR(60),
  PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE INDEX HarvesterNode_ind1 ON HarvesterNode(dt);
CREATE INDEX HarvesterNode_ind2 ON HarvesterNode(uid);
CREATE INDEX HarvesterNode_ind3 ON HarvesterNode(dt, uid);

CREATE TABLE Disk (
  id BIGSERIAL,
  id_harvester_node BIGINT NOT NULL,
  uid VARCHAR(100) NOT NULL, -- send by client
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- actual time by client
  name VARCHAR(200) NOT NULL,
  path VARCHAR(200) NOT NULL,
  size BIGINT DEFAULT 0,
  plots_count INT NOT NULL,
  plots_size BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_harvester_node) REFERENCES HarvesterNode (id)
) WITHOUT OIDS;

CREATE INDEX Disk_ind1 ON Disk(dt);
CREATE INDEX Disk_ind2 ON Disk(id_harvester_node);
CREATE INDEX Disk_ind3 ON Disk(uid);

CREATE TABLE DiskSmart (
  id BIGSERIAL,
  id_disk BIGINT NOT NULL,
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- actual time by client
  Raw_Read_Error_Rate_VALUE INT,
  Raw_Read_Error_Rate_RAW_VALUE BIGINT,
  Seek_Error_Rate_VALUE INT,
  Seek_Error_Rate_RAW_VALUE BIGINT,
  Power_On_Hours_RAW_VALUE INT,
  Temperature_Celsius_VALUE INT,
  Temperature_Celsius_RAW_VALUE INT,
  Reallocated_Sector_Count_VALUE INT,
  Reallocated_Sector_Count_RAW_VALUE BIGINT,
  Spin_Retry_Count_VALUE INT,
  Spin_Retry_Count_RAW_VALUE BIGINT,
  Reallocated_Event_Count_VALUE INT,
  Reallocated_Event_Count_RAW_VALUE BIGINT,
  Current_Pending_Sector_VALUE INT,
  Current_Pending_Sector_RAW_VALUE BIGINT,
  Offline_Uncorrectable_VALUE INT,
  Offline_Uncorrectable_RAW_VALUE BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (id_disk) REFERENCES Disk (id)
) WITHOUT OIDS;

CREATE INDEX DiskSmart_ind1 ON DiskSmart(dt);
CREATE INDEX DiskSmart_ind2 ON DiskSmart(id_disk);
CREATE INDEX DiskSmart_ind3 ON DiskSmart(id_disk,dt);

CREATE TABLE ReadAccess (
  id BIGSERIAL,
  id_disk BIGINT NOT NULL,
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- actual time by client
  filename VARCHAR(200),
  seek_at BIGINT,
  read_size BIGINT,
  read_time BIGINT,
  success BOOLEAN NOT NULL,
  info VARCHAR(400),
  PRIMARY KEY (id),
  FOREIGN KEY (id_disk) REFERENCES Disk (id)
) WITHOUT OIDS;

CREATE INDEX ReadAccess_ind1 ON ReadAccess(dt);
CREATE INDEX ReadAccess_ind2 ON ReadAccess(id_disk);
CREATE INDEX ReadAccess_ind3 ON ReadAccess(dt, id_disk);

CREATE TABLE LogFarm (
  id BIGSERIAL,
  id_harvester_node BIGINT NOT NULL,
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- time in log
  plots INT,
  block VARCHAR(150),
  proofs INT,
  time INT,
  plots_total INT,
  PRIMARY KEY (id),
  FOREIGN KEY (id_harvester_node) REFERENCES HarvesterNode (id)
) WITHOUT OIDS;

CREATE INDEX LogFarm_ind1 ON LogFarm(dt);
CREATE INDEX LogFarm_ind2 ON LogFarm(id_harvester_node);
CREATE INDEX LogFarm_ind3 ON LogFarm(dt,id_harvester_node);

CREATE TABLE LogError (
  id BIGSERIAL,
  id_harvester_node BIGINT NOT NULL,
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- time in log
  PRIMARY KEY (id),
  FOREIGN KEY (id_harvester_node) REFERENCES HarvesterNode (id)
) WITHOUT OIDS;

CREATE INDEX LogError_ind1 ON LogError(dt);
CREATE INDEX LogError_ind2 ON LogError(id_harvester_node);
CREATE INDEX LogError_ind3 ON LogError(dt,id_harvester_node);

CREATE TABLE ClientAccess (
  id BIGSERIAL,
  uid VARCHAR(100), -- send by client
  dt TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  ip VARCHAR(60),
  cmd VARCHAR(100),
  success BOOLEAN NOT NULL,
  PRIMARY KEY (id)
) WITHOUT OIDS;
   
CREATE INDEX ClientAccess_ind1 ON ClientAccess(dt);
CREATE INDEX ClientAccess_ind2 ON ClientAccess(uid);

