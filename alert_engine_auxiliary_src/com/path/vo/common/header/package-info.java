/**
 * @author MohammadAliMezzawi
 */
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value=DateAdapter.class, type=Date.class),
})

package com.path.vo.common.header;
import java.util.Date;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import com.path.lib.common.util.DateAdapter;
import com.path.lib.common.util.DateTimeAdapter;
