echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='
echo '============ Verificando Regras de Coverage ========'
echo '============ Integração com SonarCloud ========'


# Rodando os testes
mvn -Ptest -D spring.profiles.active=test test sonar:sonar -D sonar.login=$SONAR_API_KEY -D sonar.java.binaries=target/classes




echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '======================== Fim ========================'