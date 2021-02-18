echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='
echo '============ Verificando Regras de Coverage ========'


# Rodando os testes
# mvn clean test -P test -Djacoco.skip=true
mvn clean verify sonar:sonar -Pcoverage -Dsonar.projectKey=tperrut_petz-api


# rules configuradas no POM
# mvn clean verify -P coverage


echo '========================================================'
echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '=================== Fim  ====================='
