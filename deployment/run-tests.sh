echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='
echo '============ Verificando Regras de Coverage ========'


# Rodando os testes
# mvn clean test -P test -Djacoco.skip=true
# mvn clean verify install sonar:sonar \
 mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar  
#-Pcoverage \
#-Dsonar.projectKey=tperrut_petz-api \
#-Dsonar.organization=tperrut \
#-Dsonar.host.url=https://sonarcloud.io \









echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '======================== Fim ========================'