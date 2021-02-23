echo '========================================================' 
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '================= Iniciando os Testes =================='
echo '============ Verificando Regras de Coverage ========'
echo '============ Integração com SonarCloud ========'


# Rodando os testes
mvn -P test clean  sonar:sonar -Dsonar.login=$SONAR_API_KEY 




echo '========================================================'
echo '====================  ¯\_༼ᴼل͜ᴼ༽_/¯‍  ======================'
echo '======================== Fim ========================'