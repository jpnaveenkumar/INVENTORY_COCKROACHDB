cockroach sql --user=root --host=localhost --port=26257  --insecure < helper.sql

cockroach sql --insecure --host=localhost:26257

cockroach start-single-node --insecure