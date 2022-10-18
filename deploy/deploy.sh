chmod 0600 ./deploy/sept

# Reset the server
ssh -i ./deploy/sept -o StrictHostKeyChecking=no root@95.111.217.168 './reset.sh'
# Copy file
scp -o StrictHostKeyChecking=no -i ./deploy/sept -rp ./BackEnd  root@95.111.217.168:/root
# start docker compose
ssh -i ./deploy/sept -o StrictHostKeyChecking=no root@95.111.217.168 './run.sh'
