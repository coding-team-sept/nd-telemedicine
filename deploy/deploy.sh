chmod 0600 ./deploy/sept

# Reset the server
ssh -i ./deploy/sept -o StrictHostKeyChecking=no root@95.111.217.168 './reset.sh'
# Copy file
scp -i ./deploy/sept -rp ../BackEnd -o StrictHostKeyChecking=no root@95.111.217.168:/root
# start docker compose
ssh -i ./deploy/sept -o StrictHostKeyChecking=no root@95.111.217.168 './run.sh'
