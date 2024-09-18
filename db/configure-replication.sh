#!/bin/sh

# Define the master database and the replicas
master_db="logrepl_pg_master"
replica="logrepl_pg_replica1"

docker exec -i "$master_db" psql -U master -d card_db -c "CREATE TABLE card_ranges(
                                                        bin        VARCHAR(6) PRIMARY KEY,
                                                        min_range  VARCHAR(19),
                                                        max_range  VARCHAR(19),
                                                        alpha_code VARCHAR(2),
                                                        bank_name  VARCHAR(255)
                                                        );"
docker exec -i "$master_db" psql -U master -d card_db -c "DROP PUBLICATION IF EXISTS my_publication;"
docker exec -i "$master_db" psql -U master -d card_db -c "CREATE PUBLICATION my_publication FOR ALL TABLES;"


echo "Configuring $replica to replicate from $master_db..."
docker exec -i "$replica" psql -U replica -d card_db -c "CREATE TABLE card_ranges(
                                                        bin        VARCHAR(6) PRIMARY KEY,
                                                        min_range  VARCHAR(19),
                                                        max_range  VARCHAR(19),
                                                        alpha_code VARCHAR(2),
                                                        bank_name  VARCHAR(255)
                                                        );"
docker exec -i "$replica" psql -U replica -d card_db -c "DROP SUBSCRIPTION IF EXISTS logrepl_pg_replica1_subscription;"
docker exec -i "$replica" psql -U replica -d card_db -c "CREATE SUBSCRIPTION logrepl_pg_replica1_subscription_subscription CONNECTION 'dbname=card_db host=logrepl_pg_master user=master password=master' PUBLICATION my_publication;"


echo "$replica configured to replicate from $master_db."
echo "Master and replicas configured successfully for logical replication."
