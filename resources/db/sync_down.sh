#!/bin/bash
# do a backup first so that progress can't be accidentally lost.
TIMESTAMP=$(date +%Y%m%d-%H%M%S)
BACKUP_NAME="${TIMESTAMP}_from_sync_latin.db"
cp ./latin.db $BACKUP_NAME
scp root@143.198.140.191:/home/sync/latin.db .

