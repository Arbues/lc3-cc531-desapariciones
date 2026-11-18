# Presentación y guion LC3

- presentacion.tex: Beamer de 37 láminas, con 19 principales y el resto como anexos.
- estilo.tex: estilo, diagramas TikZ y carga de fragmentos del código original.
- consultas/ y secciones/: contenido de las láminas.
- guion-exposicion.tex: entrada del guion literal para ambos expositores.
- [guion-exposicion.md](guion-exposicion.md): copia legible del desarrollo completo, la ruta breve y las preguntas, disponible sin compilar.
- guion/detallado/: lectura explicada de las 37 láminas, con origen de las preguntas, cuadros, flechas, operaciones, términos, salidas y transiciones.
- guion/01-recorrido.tex: alternativa breve para ensayar diez minutos.
- guion/02-ampliaciones.tex: explicaciones de reserva.
- guion/03-anexos-y-preguntas.tex: respuestas al docente y texto para la demo.
- DEMO.md: preparación, comandos y salidas esperadas.
- demo/compose.yaml: entorno Docker preparado; no se ejecutó durante la revisión de estos materiales.

La versión explicada tiene alrededor de 11 000 palabras habladas: aproximadamente
75–95 minutos a 120–145 palabras por minuto, sin pausas ni demo. Sirve para estudiar
y responder preguntas. La ruta breve es una alternativa con objetivo de 9:30 minutos;
necesita ensayo cronometrado. No se deben leer ambas consecutivamente en diez minutos.

## Estado de los PDF

Ambos PDF están compilados desde las fuentes actuales: presentacion.pdf tiene 37 láminas
y guion-exposicion.pdf tiene 33 páginas con las 37 láminas explicadas, la ruta breve,
las ampliaciones, el reparto de tiempos y las respuestas al docente.

Se compilan con pdfTeX y las fuentes Type1 de `cm-super` (paquete `cm-super`). Sin ese
paquete, pdfTeX sustituye las fuentes por mapas de bits Type3: el texto se ve borroso al
proyectar y el archivo pesa menos. Comprobar con `kpsewhich sfrm1000.pfb`.

Para regenerar ambos PDF, ejecutar desde esta carpeta:

    latexmk -pdf -interaction=nonstopmode -halt-on-error presentacion.tex guion-exposicion.tex

Los fragmentos de las láminas se leen de ../code/; conservar la estructura del
repositorio al copiar o compilar. Las cifras proceden de ../evidencias/salidas/
y de los controles PostgreSQL; esta revisión no cambia las consultas ni sus resultados.
