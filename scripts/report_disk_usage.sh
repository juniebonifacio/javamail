#!/usr/bin/env bash
#title          :report_diskusage.sh
#description    :Report Disk usage for this application.
#author         :Junie D. Bonifacio, MTM
#date           :20141029
#version        :0.1    
#usage          :./report_diskusage.sh
#notes          :       
#bash_version   :4.1.2(1)-release
#============================================================================


OIFS="${IFS}"
NIFS=$'\n'
IFS="${NIFS}"

threshold=60
diskspace=`df -hT /dev/mapper/vg_kaos-lv_root | awk '{print $5}' | sed '3!d'`
diskspacenumberonly=`df -hT /dev/mapper/vg_kaos-lv_root | awk '{print $5}' | sed '3!d' | cut -c 1-2`
echo ${diskspace}

project=Solutio
environment=Production

subject="[DevOps] ${project} ${environment} disk usage reached ${diskspace}. Check asap. "
echo ${subject}

content=$(df -hT)

echo "${diskspacenumberonly} -ge ${threshold}" 
if [ "${diskspacenumberonly}" -ge "${threshold}" ] ; then
 
     htmlfile=/opt/devops/scripts/contentfile.html

     rm ${htmlfile}
     touch ${htmlfile}

     echo "<html>" >> ${htmlfile}

     for LINE in ${content} ; do

          echo "<p>" >> ${htmlfile}

          IFS="${OIFS}"

          echo "${LINE}" >> ${htmlfile}

          IFS="${NIFS}"

          echo "</p>" >> ${htmlfile}

     done

     echo "</html>" >> ${htmlfile}

     java -jar /opt/devops/scripts/quickmail-jar-with-dependencies.jar ${subject} ${htmlfile}

fi
