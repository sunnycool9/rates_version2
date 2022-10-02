# compile thde code and publish the docker image to docker.io
/opt/maven1/mvn1/bin/mvn -Dmaven.test.skip=true clean compile package
docker build -t sunnycool17/rates1:v2 .
docker push sunnycool17/rates1:v2

# Add database secrets to project namespace
oc apply -f ../rates-info-db-secrets.yaml -n ratesinfo-project

# Create Environment Variables using the secrets
oc set env --prefix=RATESPRJ_ENV_ --from=secret/rates-prjsecret deployment/ratesinfo1 -n ratesinfo-project

#oc import-image ratesinfo-project/ratesinfo1:main --from="docker.io/sunnycool17/rates1:v2" --confirm
oc tag docker.io/sunnycool17/rates1:v2 ratesinfo-project/ratesinfo1:main
