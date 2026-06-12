#!/bin/bash

# Configuration
PROJECT_NAME="neksus"
JAR_NAME="${PROJECT_NAME}.jar"
SRC_DIR="src"
OUT_DIR="bin"
LIB_DIR="lib"
TARGET_DIR="../test/src/main/webapp/WEB-INF/lib"

echo "=== Début de la construction du framework $PROJECT_NAME ==="

# 1. Nettoyer et créer le répertoire de sortie
rm -rf $OUT_DIR
mkdir -p $OUT_DIR

# 2. Construire le classpath avec les fichiers dans lib/ (comme servlet-api.jar)
CLASSPATH=""
if [ -d "$LIB_DIR" ]; then
    for jar in $LIB_DIR/*.jar; do
        if [ "$CLASSPATH" != "" ]; then
            CLASSPATH="$CLASSPATH:"
        fi
        CLASSPATH="$CLASSPATH$jar"
    done
fi

# 3. Récupérer tous les fichiers sources Java
SOURCES=$(find $SRC_DIR -name "*.java")
if [ -z "$SOURCES" ]; then
    echo "❌ Aucun fichier .java trouvé dans $SRC_DIR"
    exit 1
fi

# 4. Compiler les classes
echo "🔨 Compilation des fichiers sources..."
if [ -n "$CLASSPATH" ]; then
    javac -d $OUT_DIR -cp "$CLASSPATH" $SOURCES
else
    javac -d $OUT_DIR $SOURCES
fi

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation."
    exit 1
fi

# 5. Créer l'archive JAR
echo "📦 Création du fichier $JAR_NAME..."
jar cf $JAR_NAME -C $OUT_DIR .

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la création du JAR."
    exit 1
fi

# 6. Copier le JAR vers le projet de test
echo "🚚 Copie vers $TARGET_DIR..."
mkdir -p $TARGET_DIR
cp $JAR_NAME "$TARGET_DIR/"

if [ $? -eq 0 ]; then
    echo "✅ Fichier copié avec succès dans $TARGET_DIR/$JAR_NAME !"
else
    echo "❌ Erreur lors de la copie."
    exit 1
fi
