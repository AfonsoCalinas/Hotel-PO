#!/bin/bash

# Diretório onde está o hva.app.App (ajuste se necessário)
test_dir="auto-tests"
expected_dir="${test_dir}/expected"
result_dir="${test_dir}/result"

# Cores para a saída do terminal
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # Sem cor (reseta para a cor padrão)

GROUP="088"

cd ./"$GROUP" || { echo "Erro: Grupo $GROUP não encontrado."; exit 1; }
make
cd ../POTests

# Cria o diretório de resultados, caso não exista
mkdir -p "$result_dir"

# Contadores de testes
total_tests=0
correct_tests=0

# Array para armazenar os resultados dos testes
declare -a test_results

# Loop para iterar sobre todos os arquivos .in no diretório de testes
for in_file in ${test_dir}/*.in; do
    # Incrementa o contador de testes totais
    ((total_tests++))

    # Extrai o prefixo do nome do arquivo (removendo a extensão .in)
    prefix=$(basename "$in_file" .in)

    # Extract only the test number (e.g., 01-08) from the prefix and show a tick or cross
    test_number=$(echo "$prefix" | grep -oP '\d{2}-\d{2}')

    # Criação dos caminhos dos arquivos relacionados
    import_file="${test_dir}/${prefix}.import"
    out_file="${expected_dir}/${prefix}.out"
    result_file="${result_dir}/${prefix}.outhyp"

    # Verifica se o arquivo de saída esperado existe
    if [[ ! -f "$out_file" ]]; then
        continue
    fi

    # Executa o programa com os arquivos de teste e verifica se existe import ou não
    if [[ ! -f "$import_file" ]]; then
        java -Din=${in_file} -Dout=${result_file} hva.app.App
    else
        java -Dimport=${import_file} -Din=${in_file} -Dout=${result_file} hva.app.App
    fi

    # Compara a saída gerada com o arquivo de referência
    diff -b "${out_file}" "${result_file}"

    

    # Mostra uma mensagem de sucesso ou insucesso e salva o resultado no array
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Test ${prefix}: Success - No differences${NC}"
        test_results+=("${test_number}: ✔")  # Saves only the test number and success symbol
        ((correct_tests++))  # Incrementa o contador de testes corretos
    else
        echo -e "${RED}Test ${prefix}: Failure - Differences found${NC}"
        test_results+=("${test_number}: ✖")  # Saves only the test number and failure symbol
    fi
done

# Exibe a lista de todos os testes e seus resultados com cores
echo -e "\nTest Results Summary:"
for result in "${test_results[@]}"; do
    echo -e "$result"
done

# Calcula a porcentagem de testes corretos
if [ $total_tests -gt 0 ]; then
    percentage=$(( 100 * correct_tests / total_tests ))
    echo -e "\nTotal Tests: $correct_tests/$total_tests ($percentage%) passed"
else
    echo -e "\nNo tests were executed."
fi

rm -f *.dat
