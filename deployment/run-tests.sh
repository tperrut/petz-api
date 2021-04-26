echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='
echo '============ Verificando Regras de Coverage ========'
echo '============ Integração com SonarCloud ========'


# Rodando os testes
mvn -Ptest -Dspring.profiles.active=test test sonar:sonar -Dsonar.login=$SONAR_API_KEY -Dsonar.java.binaries=target/classes




echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '======================== Fim ========================'