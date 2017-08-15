The Mac OSX executables have been copied out of the https://github.com/3D-e-Chem/docker-silicos-it/tree/master/x86_64-apple-darwin Docker image.
The Docker image get the source tarballs from http://silicos-it.be.s3-website-eu-west-1.amazonaws.com/software/software.html .

Get binaries from Docker image:
```
docker create --name silicosit-darwin 3dechem/silicosit:darwin
cd bin.macosx/src/main/resources/
mkdir bin lib
docker cp silicosit-darwin:/usr/local/bin/align-it bin/align-it
docker cp silicosit-darwin:/usr/local/bin/shape-it bin/shape-it
docker cp silicosit-darwin:/usr/local/bin/strip-it bin/strip-it
docker cp silicosit-darwin:/usr/local/bin/filter-it bin/filter-it
docker cp silicosit-darwin:/usr/local/lib/libinchi.so.0.4.1 lib/
docker cp silicosit-darwin:/usr/local/lib/libopenbabel.so.5.0.0 lib/
docker cp silicosit-darwin:/usr/local/lib/openbabel/2.4.1/ lib/
mv lib/2.4.1 lib/plugins
docker rm -f silicosit-darwin 
```
