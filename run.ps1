# Script para ejecutar el microservicio de Microfinanzas
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "  Iniciando Microservicio de Microfinanzas" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que MySQL este corriendo
Write-Host "Verificando MySQL..." -ForegroundColor Yellow
$mysqlRunning = Get-Process mysqld -ErrorAction SilentlyContinue
if ($null -eq $mysqlRunning) {
    Write-Host "ADVERTENCIA: MySQL no parece estar ejecutandose." -ForegroundColor Red
    Write-Host "Por favor, inicia MySQL antes de continuar." -ForegroundColor Red
    Write-Host ""
    $continue = Read-Host "Deseas continuar de todas formas? (s/n)"
    if ($continue -ne 's' -and $continue -ne 'S') {
        exit
    }
} else {
    Write-Host "MySQL esta ejecutandose OK" -ForegroundColor Green
}

Write-Host ""
Write-Host "Compilando y ejecutando la aplicacion..." -ForegroundColor Yellow
Write-Host ""

# Ejecutar Maven
.\mvnw.cmd clean spring-boot:run

