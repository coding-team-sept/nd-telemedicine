chmod 0600 ./sept

# Reset the server
ssh -i ./sept root@95.111.217.168 './reset.sh'
# Copy file
scp -i ./sept -rp ../BackEnd root@95.111.217.168:/root
# start docker compose
ssh -i ./sept root@95.111.217.168 './run.sh'
