<?xml version='1.0' ?>
<xsl:stylesheet 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:str="http://exslt.org/strings" extension-element-prefixes="str"
		version="1.0"
		>
  <xsl:output method="xml" encoding="UTF-8"/>
	<xsl:param name="version" />
  <xsl:param name="langs" select="'en fr de it es pt tr el eu bg ca cs ru hr ms ja ro pl si ko da iw ta kn'"/>

  <xsl:template match="/">
    <xsl:for-each select="str:tokenize($langs)">
        <xsl:call-template name="extract">
          <xsl:with-param name="lang" select="."/>
        </xsl:call-template>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="extract">
    <xsl:param name="lang"/>
    <xsl:variable name="dir">
      <xsl:text>../myExpenses/src/main/res/values</xsl:text>
      <xsl:choose>
        <xsl:when test="$lang='en'"></xsl:when>
        <xsl:otherwise>-<xsl:value-of select="$lang"/></xsl:otherwise>
      </xsl:choose>
      <xsl:text>/upgrade.xml</xsl:text>
    </xsl:variable>
    <xsl:variable name="changelog">
    <xsl:for-each select="str:tokenize($version)">
    <xsl:apply-templates select="document($dir)/resources/string-array">
    <xsl:with-param name="version" select="."/>
    </xsl:apply-templates>
    </xsl:for-each>
    </xsl:variable>
    <xsl:if test="$changelog != ''">
      <xsl:variable name="element-name">
        <xsl:call-template name="map-element-name">
          <xsl:with-param name="lang" select="$lang"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:element name="{$element-name}">
      <xsl:text>
</xsl:text>
      <xsl:value-of select="$changelog"/>
      </xsl:element><xsl:text>
</xsl:text>
    </xsl:if>        
  </xsl:template>

  <xsl:template match="string-array">
   <xsl:param name="version"/>
    <xsl:variable name="version_short" select="str:replace($version,'.','')"/>
    <xsl:if test="@name=concat('whats_new_',$version_short)">
     <xsl:apply-templates select='item'/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="item">
  <xsl:variable name="apos">'</xsl:variable>
  <xsl:variable name="quote">"</xsl:variable>
    <xsl-text>• </xsl-text>
    <xsl:value-of select="concat('',str:replace(str:replace(.,concat('\',$apos),$apos),concat('\',$quote),$quote),'.')" /><xsl:text>
</xsl:text>
  </xsl:template>
  
  <xsl:template name="map-element-name">
    <xsl:param name="lang"/>
    <xsl:choose>
      <xsl:when test="$lang = 'de'">de-DE</xsl:when>
      <xsl:when test="$lang = 'en'">en-US</xsl:when>
      <xsl:when test="$lang = 'es'">es-ES</xsl:when>
      <xsl:when test="$lang = 'fr'">fr-FR</xsl:when>
      <xsl:when test="$lang = 'ja'">ja-JP</xsl:when>
      <xsl:when test="$lang = 'eu'">eu-ES</xsl:when>
      <xsl:when test="$lang = 'tr'">tr-TR</xsl:when>
      <xsl:when test="$lang = 'it'">it-IT</xsl:when>
      <xsl:when test="$lang = 'iw'">iw-IL</xsl:when>
      <xsl:when test="$lang = 'pl'">pl-PL</xsl:when>
      <xsl:when test="$lang = 'ru'">ru-RU</xsl:when>
      <xsl:otherwise><xsl:value-of select="$lang"/></xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  </xsl:stylesheet>
