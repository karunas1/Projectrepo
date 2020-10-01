#!/bin/bash

echo "Started pre-remove phase for ${artifact.id}"

if [ $1 -eq 0 ]; then
	/etc/init.d/receipt-service-instance-1 stop
	chkconfig --del receipt-service-instance-1

fi
exit 0