# Build
custom_build(
    # Name of the container image
	ref = 'catalog-service',
    # Command to build the container image
	command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    # files to watch that trigger a new build
    deps = ['build.gradle.kts', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

#manage
k8s_resource('catalog-service', port_forwards=['9001'])