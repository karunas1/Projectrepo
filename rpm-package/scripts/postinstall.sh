#!/bin/bash

echo "Started post-init phase for ${artifact-id}"
chkconfig receipt-service-instance-1 on
echo 'Setting permission Base to directories...'
chmod ug+rwx,o-rwx ${rpm.install.directory}
chmod ug+rwx,o-rwx ${rpm.conf.directory}
chmod ug+rwx,o-rwx ${rpm.log.directory}
chmod ug+rwx,o+rx /var
chmod ug+rwx,o+rx /var/run
chmod ug+rwx,o+rx /var/log
chmod ug+rwx,o+rx /var/log/vcs
chmod ug+rwx,o+rx /etc
chmod ug+rwx,o+rx /etc/vcs
chmod ug+rwx,o+rx /opt
chmod ug+rwx,o+rx /opt/vcs
chmod ug+rwx,o-rwx ${rpm.solution.directory}
chmod ug+rwx,o-rwx ${rpm.solution.log.directory}
chmod ug+rwx,o-rwx ${rpm.solution.conf.directory}
echo 'Changing owner and group for installations files...'
chown -R ${rpm.username}:${rpm.groupname} ${rpm.solution.directory}
chown -R ${rpm.username}:${rpm.groupname} ${rpm.install.directory}
echo 'Changing owner and group for log files...'
chown -R ${rpm.username}:${rpm.groupname} ${rpm.log.directory}
echo 'Changing owner and group for config files...'
chown -R ${rpm.username}:${rpm.groupname} ${rpm.conf.directory}
echo 'Setting permission to directories...'
chmod -R ug+rwx,o-rwx ${rpm.install.directory}
chmod -R ug+rwx,o-rwx ${rpm.conf.directory}
chmod -R ug+rwx,o-rwx ${rpm.log.directory}
echo 'Changing owner and group for base path...'
chown ${rpm.username}:${rpm.groupname} ${rpm.solution.directory}
chown ${rpm.username}:${rpm.groupname} ${rpm.solution.log.directory}
chown ${rpm.username}:${rpm.groupname} ${rpm.solution.conf.directory}
echo '${artifact-id} installed.check configuration files.'
exit 0