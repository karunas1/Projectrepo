#!/bin/bash

echo "Started pre-init phase for ${artifact.id}"
if [ ! -e ${rpm.log.directory} ]; then
	mkdir -p ${rpm.log.directory}
fi

if [ ! -e ${rpm.log.directory}/receipt-service-instance-1/tmp ]; then
	mkdir -p ${rpm.log.directory}/receipt-service-instance-1/tmp
fi
if [ -e /etc/init.d/receipt-service-instance-1 ]; then
	/etc/init.d/receipt-service-instance-1 stop &> /dev/null
fi
if [ ! -e ${rpm.install.directory}/vault ]; then
	mkdir -p ${rpm.install.directory}/vault
fi

ret=true
getent passwd ${rpm.username} >/dev/null 2>&1 && ret=false

if $ret; then
	useradd -M -s /sbin/nologin ${rpm.username} >/dev/null 2>&1
	groupadd -f ${rpm.groupname}
	usermod -G ${rpm.groupname} -a ${rpm.username}
	echo "The new user ${rpm.username} was created successfully"
fi

exit 0