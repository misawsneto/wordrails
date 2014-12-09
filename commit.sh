find . -depth -name .classpath -exec rm -rf {} \;
find . -depth -name .project   -exec rm -rf {} \;
find . -depth -name .settings  -exec rm -rf {} \;
find . -depth -name bin        -exec rm -rf {} \;
find . -depth -name target     -exec rm -rf {} \;
