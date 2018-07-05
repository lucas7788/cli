if [ -f "./cli.jar" && -f "./ontology-sdk-java.jar" && -f "authConfig.json"]; then
  java -cp .:ontology-sdk-java.jar:cli-1.0-SNAPSHOT.jar demo.AssignOntIdsToRole
else
  echo Missing jar package
fi
