#!/bin/bash

JAR="target/kisscm-pract3-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "Выберите режим запуска:"
echo "1) Тестовый режим (test_repo.txt)"
echo "2) Реальный режим (APT repository)"
read -p "Выбор (1/2): " mode

MMD_FILE="graph.mmd"
SVG_FILE="graph.svg"

run_java() {
    java -jar "$JAR" "$@" --saveMermaid "$MMD_FILE"

    if command -v mmdc &> /dev/null; then
        mmdc -i "$MMD_FILE" -o "$SVG_FILE"
        echo "Граф сохранен в SVG_FILE"
    else
        echo "mmdc не установлен. Mermaid файл сохранен в $MMD_FILE"
        echo "Установите mmdc: npm install -g @mermaid-js/mermaid-cli"
    fi
}

if [ "$mode" == "1" ]; then
  read -p "Введите имя пакета (например: A): " pkg
  read -p "Введите версию (например: 1.0): " ver
  read -p "Введите максимальную глубину (например: 3): " depth
  read -p "Введите подстроку для фильтрации (например: Z): " filter
  read -p "Введите путь к тестовому репозиторию (по умолчанию test_repo.txt): " repo
  repo=${repo:-test_repo.txt}

  echo ""
  echo "Запуск в тестовом режиме..."
  run_java --package "$pkg" --repo "$repo" --version "$ver" --depth "$depth" --filter "$filter" --test

elif [ "$mode" == "2" ]; then
  read -p "Введите имя пакета (например: bash): " pkg
  read -p "Введите версию (например: 5.1-6ubuntu1): " ver
  read -p "Введите максимальную глубину (например: 2): " depth
  read -p "Введите подстроку для фильтрации (например: dev): " filter
  read -p "Введите URL пакета (по умолчанию http://archive.ubuntu.com/ubuntu/dists/jammy/main/binary-amd64/Packages.gz): " repo
  repo=${repo:-http://archive.ubuntu.com/ubuntu/dists/jammy/main/binary-amd64/Packages.gz}

  echo ""
  echo "Запуск в реальном режиме..."
  run_java --package "$pkg" --repo "$repo" --version "$ver" --depth "$depth" --filter "$filter"

else
  echo "Неверный выбор режима"
  exit 1
fi
