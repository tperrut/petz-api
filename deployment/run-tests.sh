echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='


# Rodando os testes
# mvn clean test -P test -Djacoco.skip=true
mvn clean verify sonar:sonar -Pcoverage -Dsonar.projectKeytperrut_petz-api
echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ====================='
echo '=================== Fim dos Testes ====================='


echo '========================================================' 
echo '========================================================'
echo '============ Verificando Regras de Coverage ========'
# rules configuradas no POM
# mvn clean verify -P coverage



echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '=================== Fim Coverage ====================='
